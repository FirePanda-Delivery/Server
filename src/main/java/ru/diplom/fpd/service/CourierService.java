package ru.diplom.fpd.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.diplom.fpd.dto.ActiveCourier;
import ru.diplom.fpd.dto.Coordinates;
import ru.diplom.fpd.dto.CourierDto;
import ru.diplom.fpd.dto.requestModel.CourierReq;
import ru.diplom.fpd.exception.CourierIsAlreadyActiveException;
import ru.diplom.fpd.mapper.CourierMapper;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.Courier;
import ru.diplom.fpd.repositories.CourierRepositories;

@Service
public class CourierService {

    private final CourierRepositories courierRepositories;
    private final CitiesServices citiesServices;
    private final Log logger = LogFactory.getLog(getClass());
    private final CourierMapper courierMapper;

    @Autowired
    public CourierService(CourierRepositories courierRepositories, CitiesServices citiesServices,
                          CourierMapper courierMapper) {
        this.courierRepositories = courierRepositories;
        this.citiesServices = citiesServices;
        this.courierMapper = courierMapper;
    }


    /**
     * get all users except deleted ones
     *
     * @return list of users without deleted
     */
    public List<CourierDto> getCourierList() {

        return courierRepositories.findByIsDeletedFalse().stream()
                .map(courierMapper::toDto)
                .toList();
    }

    /**
     * get all users
     *
     * @return list of users together with deleted ones
     */
    public List<Courier> getAll() {
        return courierRepositories.findAll();
    }

    public CourierDto get(long id) {
        if (id == 0) {
            throw new NullPointerException("id not set");
        }
        return courierMapper.toDto(courierRepositories.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Courier is not found")));
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

    public List<Courier> getByCities(City city) {

        if (city == null) {
            throw new NullPointerException("city not set");
        }
        return courierRepositories.findAllByCity(city);
    }

    public List<Courier> getByCitiesName(String cities) {

        if (cities == null || cities.isEmpty()) {
            throw new NullPointerException("city not set");
        }

        return courierRepositories.findAllByCityCityIgnoreCase(cities);
    }

    public CourierDto add(CourierReq courier) {
        return courierMapper.toDto(courierRepositories.save(courierMapper.requestToEntity(courier)));
    }

    public List<Courier> addCourierList(List<Courier> couriers) {
        if (couriers == null || couriers.isEmpty()) {
            throw new NullPointerException("couriers not set");
        }
        return courierRepositories.saveAll(couriers);
    }

    public CourierDto update(CourierDto courier) {
        if (!courierRepositories.existsById(courier.getId())) {
            throw new EntityNotFoundException("courier not found!");
        }
        return courierMapper.toDto(courierRepositories.save(courierMapper.toEntity(courier)));
    }

    public Courier courierRecovery(long id) {

        Optional<Courier> courierOptional = courierRepositories.findById(id);

        if (courierOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found!");
        }

        Courier courier = courierOptional.get();
        courier.setDeleted(false);
        return courierRepositories.save(courier);
    }

    public void delete(long id) {

        Optional<Courier> courierOptional = courierRepositories.findById(id);

        if (courierOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found!");
        }

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

    public void setCourierLocation(long id, Coordinates location) {

        if (location == null) {
            throw new NullPointerException("Location not set");
        }

        Storage.getActiveCourier(courierRepositories.findById(id).orElseThrow(NullPointerException::new))
                .setLocation(location);
    }

    @ExceptionHandler({CourierIsAlreadyActiveException.class})
    public ResponseEntity<Map<String, String>> handleCourierIsAlreadyActive(CourierIsAlreadyActiveException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new LinkedHashMap<>();
        logger.error(ex + ". " + request.toString() + ". " + Arrays.toString(ex.getStackTrace()));
        map.put("Timestamp", new Date().toString());
        map.put("Status", String.valueOf(status.value()));
        map.put("Error", status.getReasonPhrase());
        map.put("Message", ex.getMessage());
        map.put("Path", request.getContextPath());
        return ResponseEntity.status(status).body(map);
    }

    public void setActive(long id, Coordinates location) {
        CourierService.Storage.addCourier(courierRepositories.findById(id)
                .orElseThrow(EntityNotFoundException::new), location);
    }

    public void setInactive(long id) {
        ActiveCourier activeCourier = CourierService.Storage.getActiveCourier(courierRepositories.findById(id)
                .orElseThrow(EntityNotFoundException::new));
        CourierService.Storage.removeCourier(activeCourier);
    }


    public static class Storage {

        private static final Map<City, List<ActiveCourier>> activeCouriers = new LinkedHashMap<>();

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

        public static List<ActiveCourier> getNotOnOrderCouriers(City city) {

            List<ActiveCourier> courierList = new LinkedList<>();

            activeCouriers.computeIfAbsent(city, k -> new LinkedList<>());

            for (ActiveCourier activeCourier : activeCouriers.get(city)) {
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
