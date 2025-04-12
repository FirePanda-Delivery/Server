package ru.diplom.fpd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.Cities;
import ru.diplom.fpd.model.RestaurantAddress;

import java.util.List;

public interface RestaurantAddressRepositories extends JpaRepository<RestaurantAddress, Long> {

    List<RestaurantAddress> findAllByCity(Cities city);

    List<RestaurantAddress> findAllByCity_NormalizedCiti(String name);

}
