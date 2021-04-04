package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Courier;
import ru.diplom.FirePandaDelivery.model.User;

import java.util.List;

public interface CourierRepositories extends JpaRepository<Courier, Long> {

    List<Courier> findByIsDeletedFalse();

    Courier findFirstByPhone(String phone);

    List<Courier> findByIsDeletedTrue();

    List<Courier> findAllByCity(Cities city);

    List<Courier> findAllByCity_Citi(String citi);




}
