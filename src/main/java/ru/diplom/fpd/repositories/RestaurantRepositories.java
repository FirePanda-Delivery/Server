package ru.diplom.fpd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.diplom.fpd.model.Categories;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;

import java.util.*;

public interface RestaurantRepositories extends JpaRepository<Restaurant, Long>, JpaSpecificationExecutor<Restaurant> {

    List<Restaurant> findByIsDeletedFalse();
    List<Restaurant> findByIsDeletedFalseAndPublishedTrue();

    boolean existsByIdAndPublishedTrueAndIsDeletedFalse(long id);



    Optional<Restaurant> findAllByCategoriesContaining(Categories categories);
    Optional<Restaurant> findAllByCategoriesContainingAndPublishedTrueAndIsDeletedFalse(Categories categories);
    Optional<Restaurant> findAllByCategoriesContainingAndPublishedTrueAndCitiesAddressIn(Categories categories, Iterable<RestaurantAddress> citiesAddress);

    Optional<Restaurant> findByNameIgnoreCaseAndPublishedTrueAndIsDeletedFalse(String normalizedName);
    Optional<Restaurant> findByNameIgnoreCaseAndPublishedTrueAndCitiesAddressInAndIsDeletedFalse(String normalizedName, Iterable<RestaurantAddress> citiesAddress);

    List<Restaurant> findAllByCitiesAddressIn(Iterable<RestaurantAddress> citiesAddress);
    List<Restaurant> findAllByCitiesAddressInAndPublishedTrueAndIsDeletedFalse(Iterable<RestaurantAddress> citiesAddress);

}
