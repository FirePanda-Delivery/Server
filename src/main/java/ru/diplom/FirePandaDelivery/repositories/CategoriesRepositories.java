package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Categories;

import java.util.List;

public interface CategoriesRepositories extends JpaRepository<Categories, Long> {

    List<Categories> findByIsDeletedFalse();
}
