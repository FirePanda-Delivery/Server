package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Product;
import ru.diplom.FirePandaDelivery.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepositories extends JpaRepository<Categories, Long> {

    List<Categories> findByIsDeletedFalse();

    List<Categories> findByName(String name);

    Optional<Categories> findByProductsContaining(Product product);

}
