package ru.diplom.fpd.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.City;

public interface CitiesRepositories extends JpaRepository<City, Long> {

    Optional<City> findByCityIgnoreCase(String name);
}
