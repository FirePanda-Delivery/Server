package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Product;

import java.util.List;

public interface ProductRepositories extends JpaRepository<Product, Long> {

    List<Product> findByIsDeletedFalse();

    List<Product> findByNameAndIsDeletedFalse(String name);

    List<Product> findByNormalizedNameAndIsDeletedFalse(String name);
}
