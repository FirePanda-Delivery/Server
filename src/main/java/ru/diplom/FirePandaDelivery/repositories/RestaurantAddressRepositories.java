package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.RestaurantAddress;

import java.util.List;

public interface RestaurantAddressRepositories extends JpaRepository<RestaurantAddress, Long> {

    List<RestaurantAddress> findAllByCity(Cities city);

    List<RestaurantAddress> findAllByCity_NormalizedCiti(String name);

}
