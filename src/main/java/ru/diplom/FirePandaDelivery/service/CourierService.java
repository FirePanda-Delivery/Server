package ru.diplom.FirePandaDelivery.service;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.diplom.FirePandaDelivery.dto.ActiveCourier;
import ru.diplom.FirePandaDelivery.dto.Coordinates;
import ru.diplom.FirePandaDelivery.exception.CourierIsAlreadyActiveException;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Courier;
import ru.diplom.FirePandaDelivery.repositories.CourierRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class CourierService {

    private final CourierRepositories courierRepositories;
    private final CitiesServices citiesServices;
    private final Log logger =  LogFactory.getLog(getClass());

    @Autowired
    public CourierService(CourierRepositories courierRepositories, CitiesServices citiesServices) {
        this.courierRepositories = courierRepositories;
        this.citiesServices = citiesServices;
    }


    /**
     * get all users except deleted ones
     * @return list of users without deleted
     */
    public List<Courier> getCourierList() {

        return courierRepositories.findByIsDeletedFalse();
    }

    /**
     * get all users
     * @return list of users together with deleted ones
     */
    public List<Courier> getAll() {
        return courierRepositories.findAll();
    }

    public Courier getByPhone(String phone) {
        if (phone.isEmpty()) { throw new NullPointerException("phone not set"); }

        // тут будет проверка телефона(может быть)

        return courierRepositories.findFirstByPhone(phone);
    }

    public Courier get(long id) {
        if (id == 0) { throw new NullPointerException("id not set"); }
        Optional<Courier> courier = courierRepositories.findById(id);
        if (courier.isEmpty()) { throw new EntityNotFoundException("Courier is not found"); }
        return courier.get();
    }

    public List<ActiveCourier> getActiveCourierByCity(String city) {
        if (city == null || city.isEmpty()) {
            throw new NullPointerException("city not set");
        }
        return Storage.getNotOnOrderCouriers(citiesServices.getByName(city));
    }

    public List<Courier> getDeletedList() {

        return courierRepositories.findByIsDeletedTrue();
    }

    public List<Courier> getByCities(Cities cities) {

        if (cities == null) {
            throw new NullPointerException("city not set");
        }
        return courierRepositories.findAllByCity(cities);
    }

    public List<Courier> getByCitiesName(String cities) {

        if (cities == null || cities.isEmpty()) {
            throw new NullPointerException("city not set");
        }

        return courierRepositories.findAllByCity_NormalizedCiti(cities.toUpperCase(Locale.ROOT));
    }

    public Courier add(Courier courier) {
        if (courier == null) { throw new NullPointerException("courier not set"); }
        return courierRepositories.save(courier);
    }

    public List<Courier> addCourierList(List<Courier> couriers) {
        if (couriers == null || couriers.isEmpty()) {  throw new NullPointerException("couriers not set"); }
        return courierRepositories.saveAll(couriers);
    }

    public Courier update(Courier courier) {
        if (!courierRepositories.existsById(courier.getId())) {
            throw new EntityNotFoundException("courier not found!");
        }
        return courierRepositories.save(courier);
    }

    public List<Courier> updateCourierList(List<Courier> couriers) {
        if (couriers == null || couriers.isEmpty()) {
            throw new NullPointerException("Courier not set");
        }

        List<Courier> list = new ArrayList<Courier>();

        for (Courier courier: couriers) {
            list.add(update(courier));
        }

        return list;
    }

    public Courier courierRecovery(long id) {

        Optional<Courier> courierOptional = courierRepositories.findById(id);

        if (courierOptional.isEmpty()) { throw new EntityNotFoundException("user not found!"); }

        Courier courier = courierOptional.get();
        courier.setDeleted(false);
        return courierRepositories.save(courier);
    }

    public void delete(long id) {

        Optional<Courier> courierOptional = courierRepositories.findById(id);

        if (courierOptional.isEmpty()) { throw new EntityNotFoundException("user not found!"); }

        Courier courier = courierOptional.get();
        courier.setDeleted(true);

        courierRepositories.save(courier);

        if (Storage.existActiveCourier(courier)) {
            finishCourierJob(courier);
        }
    }

    public void finishCourierJob(Courier courier) {

        if (courier == null) {
            throw new NullPointerException("courier not set");
        }

        ActiveCourier activeCourier = Storage.getActiveCourier(courier);
        Storage.removeCourier(activeCourier);
    }

    public ActiveCourier courierCompletedOrder(Courier courier) {

        if (courier == null) {
            throw new NullPointerException("courier not set");
        }

        ActiveCourier activeCourier = Storage.getActiveCourier(courier);

        activeCourier.setOnOrder(false);
        return activeCourier;
    }

    public ActiveCourier courierReceivedOrder(Courier courier) {

        if (courier == null) {
            throw new NullPointerException("courier not set");
        }

        ActiveCourier activeCourier = Storage.getActiveCourier(courier);

        activeCourier.setOnOrder(true);
        return activeCourier;
    }

    public void setCourierLocation(Courier courier, Coordinates location) {

        if (courier == null || location == null) {
            throw new NullPointerException("courier or location not set");
        }

        Storage.getActiveCourier(courier).setLocation(location);
    }

    @ExceptionHandler({CourierIsAlreadyActiveException.class})
    public ResponseEntity<Map<String, String>> handleCourierIsAlreadyActive(CourierIsAlreadyActiveException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new LinkedHashMap<>();
        logger.error(ex + ". " + request.toString() + ". " + Arrays.toString(ex.getStackTrace()));
        map.put("Timestamp", new Date().toString());
        map.put("Status",  String.valueOf(status.value()));
        map.put("Error", status.getReasonPhrase());
        map.put("Message", ex.getMessage());
        map.put("Path", request.getContextPath());
        return ResponseEntity.status(status).body(map);
    }


    public static class Storage {

        private static final Map<Cities, List<ActiveCourier>> activeCouriers = new LinkedHashMap<>();

        public static ActiveCourier getActiveCourier(Courier courier) {
            List<ActiveCourier> storageCourier = activeCouriers.get(courier.getCity());

            for (ActiveCourier activeCourier : storageCourier) {
                if (activeCourier.getCourier().equals(courier)) {
                    return activeCourier;
                }
            }

            throw new EntityNotFoundException("courier not found");
        }

        public static boolean existActiveCourier(Courier courier) {

            List<ActiveCourier> activeCourierList = activeCouriers.get(courier.getCity());

            if (activeCourierList == null || activeCourierList.isEmpty()) return false;

            return activeCourierList.stream().anyMatch(activeCourier -> activeCourier.getCourier().equals(courier));
        }

        public static void addCourier(Courier courier, Coordinates location) {

            if (courier == null) {
                throw new NullPointerException();
            }

            if (existActiveCourier(courier)) {
                throw new CourierIsAlreadyActiveException("Courier " + courier.getId() + " is already active");
            }

            List<ActiveCourier> courierList = activeCouriers.get(courier.getCity());

            ActiveCourier activeCourier = new ActiveCourier();
            activeCourier.setCourier(courier);
            activeCourier.setOnOrder(false);
            activeCourier.setLocation(location);

            if (courierList == null || courierList.isEmpty()) {
                List<ActiveCourier> newList = new LinkedList<>();
                newList.add(activeCourier);
                activeCouriers.put(courier.getCity(), newList);
                return;
            }

            courierList.add(activeCourier);
        }

        public static List<ActiveCourier> getNotOnOrderCouriers(Cities cities) {

            List<ActiveCourier> courierList = new LinkedList<>();

            activeCouriers.computeIfAbsent(cities, k -> new LinkedList<>());

            for (ActiveCourier activeCourier : activeCouriers.get(cities)) {
                if (!activeCourier.isOnOrder()) {
                    courierList.add(activeCourier);
                }
            }

            return courierList;
        }

        public static void removeCourier(ActiveCourier courier) {
            activeCouriers.get(courier.getCourier().getCity()).remove(courier);
        }

    }


}
