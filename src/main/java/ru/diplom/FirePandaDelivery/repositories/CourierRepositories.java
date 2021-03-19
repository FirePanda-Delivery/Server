package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Courier;

import java.util.List;

public interface CourierRepositories extends JpaRepository<Courier, Long> {


}
