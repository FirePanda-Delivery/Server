package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Cities;

import java.util.Optional;

public interface CitiesRepositories extends JpaRepository<Cities, Long> {

    Optional<Cities> findByCiti(String name);

    Optional<Cities> findByNormalizedCiti(String name);

}
