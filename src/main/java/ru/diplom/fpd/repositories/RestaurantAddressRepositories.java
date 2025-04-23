package ru.diplom.fpd.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.RestaurantAddress;

public interface RestaurantAddressRepositories extends JpaRepository<RestaurantAddress, Long> {

    List<RestaurantAddress> findAllByCity(City city);

    @Query("select r from RestaurantAddress r where r.city.city = :name")
    List<RestaurantAddress> findAllByCityIgnoreCase(String name);

    @Query("select r from RestaurantAddress r where r.city = ?1 and upper(r.address) = upper(?2) and r.restaurant.id = ?3")
    Optional<RestaurantAddress> findByCityAndAddress(City first, String address, long restaurantId);
}
