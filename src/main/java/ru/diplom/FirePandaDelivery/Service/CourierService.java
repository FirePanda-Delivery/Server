package ru.diplom.FirePandaDelivery.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.Courier;
import ru.diplom.FirePandaDelivery.model.User;
import ru.diplom.FirePandaDelivery.repositories.CourierRepositories;
import ru.diplom.FirePandaDelivery.repositories.UserRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourierService {

    private CourierRepositories courierRepositories;

    @Autowired
    public CourierService(CourierRepositories courierRepositories) {
        this.courierRepositories = courierRepositories;
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

    public List<Courier> getDeletedList() {

        return
        courierRepositories.findByIsDeletedTrue();
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
        if (id == 0) { throw new NullPointerException("id not set"); }
        Optional<Courier> courierOptional = courierRepositories.findById(id);

        if (courierOptional.isEmpty()) { throw new EntityNotFoundException("user not found!"); }

        Courier courier = courierOptional.get();
        courier.setDeleted(false);
        return courierRepositories.save(courier);
    }

    public void delete(long id) {

        if (id == 0) { throw new NullPointerException("id not set"); }
        Optional<Courier> courierOptional = courierRepositories.findById(id);

        if (courierOptional.isEmpty()) { throw new EntityNotFoundException("user not found!"); }

        Courier courier = courierOptional.get();
        courier.setDeleted(true);

        courierRepositories.save(courier);
    }

}
