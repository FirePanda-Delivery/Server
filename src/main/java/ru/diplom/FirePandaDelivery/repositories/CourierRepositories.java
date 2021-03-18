package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Courier;

public interface CourierRepositories extends JpaRepository<Courier, Long> {
}
