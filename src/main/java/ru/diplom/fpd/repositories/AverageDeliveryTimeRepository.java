package ru.diplom.fpd.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.AverageDeliveryTime;

public interface AverageDeliveryTimeRepository extends JpaRepository<AverageDeliveryTime, Long> {

    Optional<AverageDeliveryTime> findByRestaurant_Id(long restaurantId);
}