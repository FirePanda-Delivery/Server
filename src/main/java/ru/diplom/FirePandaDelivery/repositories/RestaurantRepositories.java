package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Restaurant;
import ru.diplom.FirePandaDelivery.model.RestaurantAddress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface RestaurantRepositories extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByIsDeletedFalse();
    List<Restaurant> findByIsDeletedFalseAndPublishedTrue();

    Optional<Restaurant> findAllByCategoriesContaining(Categories categories);
    Optional<Restaurant> findAllByCategoriesContainingAndPublishedTrue(Categories categories);

    List<Restaurant> findAllByCitiesAddressIn(Iterable<RestaurantAddress> citiesAddress);
    List<Restaurant> findAllByCitiesAddressInAndPublishedTrue(Iterable<RestaurantAddress> citiesAddress);

}
