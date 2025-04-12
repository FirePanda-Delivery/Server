package ru.diplom.fpd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.Categories;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;

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
