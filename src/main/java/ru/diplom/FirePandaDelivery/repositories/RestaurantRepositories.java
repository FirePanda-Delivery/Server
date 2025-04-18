package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Restaurant;
import ru.diplom.FirePandaDelivery.model.RestaurantAddress;

import java.util.*;

public interface RestaurantRepositories extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByIsDeletedFalse();
    List<Restaurant> findByIsDeletedFalseAndPublishedTrue();

    boolean existsByIdAndPublishedTrueAndIsDeletedFalse(long id);



    Optional<Restaurant> findAllByCategoriesContaining(Categories categories);
    Optional<Restaurant> findAllByCategoriesContainingAndPublishedTrueAndIsDeletedFalse(Categories categories);
    Optional<Restaurant> findAllByCategoriesContainingAndPublishedTrueAndCitiesAddressIn(Categories categories, Iterable<RestaurantAddress> citiesAddress);

    Optional<Restaurant> findByNormalizedNameAndPublishedTrueAndIsDeletedFalse(String normalizedName);
    Optional<Restaurant> findByNormalizedNameAndPublishedTrueAndCitiesAddressInAndIsDeletedFalse(String normalizedName, Iterable<RestaurantAddress> citiesAddress);

    List<Restaurant> findAllByCitiesAddressIn(Iterable<RestaurantAddress> citiesAddress);
    List<Restaurant> findAllByCitiesAddressInAndPublishedTrueAndIsDeletedFalse(Iterable<RestaurantAddress> citiesAddress);

}
