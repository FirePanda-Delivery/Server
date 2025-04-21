package ru.diplom.fpd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.diplom.fpd.model.Cities;
import ru.diplom.fpd.model.RestaurantAddress;

import java.util.List;

public interface RestaurantAddressRepositories extends JpaRepository<RestaurantAddress, Long> {

    List<RestaurantAddress> findAllByCity(Cities city);

    @Query("select RestaurantAddress from RestaurantAddress r where r.city.city = :name")
    List<RestaurantAddress> findAllByCityIgnoreCase(String name);

}
