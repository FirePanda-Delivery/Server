package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Restaurant;

public interface RestaurantRepositories extends JpaRepository<Restaurant, Long> {

}
