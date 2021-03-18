package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Cities;

public interface CitiesRepositories extends JpaRepository<Cities, Long> {
}
