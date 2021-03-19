package ru.diplom.FirePandaDelivery.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Restaurant;
import ru.diplom.FirePandaDelivery.repositories.CategoriesRepositories;
import ru.diplom.FirePandaDelivery.repositories.ProductRepositories;
import ru.diplom.FirePandaDelivery.repositories.RestaurantRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepositories restaurantRepositories;
    private final CategoriesRepositories categoriesRepositories;
    private final ProductRepositories productRepositories;

    @Autowired
    public RestaurantService(RestaurantRepositories restaurantRepositories, CategoriesRepositories categoriesRepositories, ProductRepositories productRepositories) {
        this.restaurantRepositories = restaurantRepositories;
        this.categoriesRepositories = categoriesRepositories;
        this.productRepositories = productRepositories;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantRepositories.findAll();
    }

    public Restaurant getRestaurant(long id) {

        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(id);
        if (optionalRestaurant.isEmpty()) { throw new NullPointerException("Restaurant is not found"); }
        return optionalRestaurant.get();
    }

    public List<Categories> getRestaurantCategories(long restaurantId) {

        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(restaurantId);
        if (optionalRestaurant.isEmpty()) { throw new EntityNotFoundException("Restaurant is not found"); }
        return optionalRestaurant.get().getCategories();
    }

    public List<Categories> getAllCategories() {

        return categoriesRepositories.findAll();
    }

    public List<Categories> getCategoriesList() {
        return categoriesRepositories.findByIsDeletedFalse();
    }

    public Categories getCategories(long id) {
        Optional<Categories> optionalCategories = categoriesRepositories.findById(id);
        if (optionalCategories.isEmpty()) { throw new EntityNotFoundException("Categories is not found"); }
        return optionalCategories.get();
    }


}
