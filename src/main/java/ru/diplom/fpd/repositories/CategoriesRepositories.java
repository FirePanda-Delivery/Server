package ru.diplom.fpd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.Categories;
import ru.diplom.fpd.model.Product;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepositories extends JpaRepository<Categories, Long> {

    List<Categories> findByIsDeletedFalse();

    List<Categories> findByName(String name);

    List<Categories> findByNameIgnoreCase(String name);

    Optional<Categories> findByProductsContaining(Product product);

}
