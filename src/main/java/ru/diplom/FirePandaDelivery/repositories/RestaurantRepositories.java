package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepositories extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByIsDeletedFalse();

    Optional<Restaurant> findAllByCategoriesContaining(Categories categories);

    List<Restaurant> findAllByCitiesContaining(Cities cities);



}
