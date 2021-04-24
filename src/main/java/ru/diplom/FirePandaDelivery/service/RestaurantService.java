package ru.diplom.FirePandaDelivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.*;
import ru.diplom.FirePandaDelivery.repositories.CategoriesRepositories;
import ru.diplom.FirePandaDelivery.repositories.ProductRepositories;
import ru.diplom.FirePandaDelivery.repositories.RestaurantAddressRepositories;
import ru.diplom.FirePandaDelivery.repositories.RestaurantRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
public class RestaurantService {

    private final RestaurantRepositories restaurantRepositories;
    private final CategoriesRepositories categoriesRepositories;
    private final ProductRepositories productRepositories;
    private final CitiesServices citiesServices;
    private final RestaurantAddressRepositories addressRepositories;

    @Autowired
    public RestaurantService(RestaurantRepositories restaurantRepositories, CategoriesRepositories categoriesRepositories, ProductRepositories productRepositories, CitiesServices citiesServices, RestaurantAddressRepositories addressRepositories) {
        this.restaurantRepositories = restaurantRepositories;
        this.categoriesRepositories = categoriesRepositories;
        this.productRepositories = productRepositories;
        this.citiesServices = citiesServices;
        this.addressRepositories = addressRepositories;
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantRepositories.findByIsDeletedFalse();
    }

    public List<Restaurant> getAllRestaurant() {
        return restaurantRepositories.findAll();
    }

    public Restaurant getRestaurant(long id) {

        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(id);
        if (optionalRestaurant.isEmpty()) { throw new EntityNotFoundException("Restaurant is not found"); }
        return optionalRestaurant.get();
    }

    public List<Restaurant> getRestaurantsByCategoryName(String name) {

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Categories category : categoriesRepositories.findByNormalizedName(name.toUpperCase(Locale.ROOT))) {

            Optional<Restaurant> optionalRestaurant = restaurantRepositories.findAllByCategoriesContaining(category);
            if (optionalRestaurant.isEmpty()) { throw new EntityNotFoundException("Restaurant is not found"); }

            restaurantList.add(optionalRestaurant.get());
        }

        return restaurantList;
    }

    public List<Restaurant> getRestaurantsByProductName(String name) {

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Product product : productRepositories.findByNormalizedNameAndIsDeletedFalse(name.toUpperCase(Locale.ROOT).trim())) {

            Optional<Categories> optionalCategory = categoriesRepositories.findByProductsContaining(product);
            if (optionalCategory.isEmpty()) { throw new EntityNotFoundException("Category is not found"); }

            Optional<Restaurant> optionalRestaurant = restaurantRepositories.findAllByCategoriesContaining(optionalCategory.get());
            if (optionalRestaurant.isEmpty()) { throw new EntityNotFoundException("Restaurant is not found"); }

            restaurantList.add(optionalRestaurant.get());
        }

        return restaurantList;
    }

    public List<Restaurant> getRestaurantsByCityName(String name) {

        return restaurantRepositories.findAllByCitiesAddressIn(
                addressRepositories.findAllByCity_NormalizedCiti(
                        name.toUpperCase(Locale.ROOT)
                )
        );
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
        if (optionalCategories.isEmpty()) {
            throw new EntityNotFoundException("Categories is not found");
        }
        return optionalCategories.get();
    }

    public List<Product> getProductsFromCategory(long catId) {

        Optional<Categories> optionalCategory = categoriesRepositories.findById(catId);

        if (optionalCategory.isEmpty()) { throw new NullPointerException("Category is not found"); }

        return optionalCategory.get().getProducts();
    }

    public List<Product> getProductList() {
        return productRepositories.findByIsDeletedFalse();
    }

    public List<Product> getAllProduct() {
        return productRepositories.findAll();
    }

    public Product getProduct(long id) {
        Optional<Product> optionalProduct = productRepositories.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new EntityNotFoundException("Product is not found");
        }
        return optionalProduct.get();
    }

    public Restaurant add(Restaurant restaurant) {



        if (restaurant == null) {
            throw new NullPointerException("restaurant not set");
        }
        return restaurantRepositories.save(restaurant);
    }

    public List<Restaurant> addList(List<Restaurant> restaurants) {
        if (restaurants == null) {
            throw new NullPointerException("restaurants not set");
        }
        if (restaurants.isEmpty()) {
            throw new NullPointerException("restaurant list is empty");
        }
        return restaurantRepositories.saveAll(restaurants);
    }

    public List<Categories> addCategory(long restId, Categories category) {
        if (category == null) {
            throw new NullPointerException("category not set");
        }

        if (!restaurantRepositories.existsById(restId)) {
            throw new EntityNotFoundException("Restaurant is not found");
        }

        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(restId);
        if (optionalRestaurant.isEmpty()) { throw new NullPointerException("Restaurant is not found"); }
        Restaurant restaurant = optionalRestaurant.get();
        restaurant.getCategories().add(category);
        restaurant = restaurantRepositories.save(restaurant);
        return restaurant.getCategories();
    }


    public List<Categories> addCategoryList(long restId, List<Categories> categories) {

        if (categories == null) {
            throw new NullPointerException("categories not set");
        }

        if (categories.isEmpty()) {
            throw new NullPointerException("categories is empty");
        }

        if (!restaurantRepositories.existsById(restId)) {
            throw new EntityNotFoundException("Restaurant is not found");
        }

        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(restId);
        if (optionalRestaurant.isEmpty()) { throw new NullPointerException("Restaurant is not found"); }
        Restaurant restaurant = optionalRestaurant.get();
        restaurant.getCategories().addAll(categories);
        restaurant = restaurantRepositories.save(restaurant);
        return restaurant.getCategories();
    }

    public List<Product> addProduct(long catId, Product product) {
        if (product == null) {
            throw new NullPointerException("products not set");
        }

        if (!categoriesRepositories.existsById(catId)) {
            throw new EntityNotFoundException("Categories is not found");
        }

        Optional<Categories> optionalCategories = categoriesRepositories.findById(catId);
        if (optionalCategories.isEmpty()) { throw new NullPointerException("Restaurant is not found"); }
        Categories category = optionalCategories.get();
        category.getProducts().add(product);
        category = categoriesRepositories.save(category);
        return category.getProducts();
    }

    public List<Product> addProductList(long catId, List<Product> products) {

        if (products == null) {
            throw new NullPointerException("products not set");
        }

        if (products.isEmpty()) {
            throw new NullPointerException("products is empty");
        }

        if (!categoriesRepositories.existsById(catId)) {
            throw new EntityNotFoundException("Categories is not found");
        }

        Optional<Categories> optionalCategories = categoriesRepositories.findById(catId);
        if (optionalCategories.isEmpty()) { throw new NullPointerException("Categories is not found"); }
        Categories categories = optionalCategories.get();
        categories.getProducts().addAll(products);
        categories = categoriesRepositories.save(categories);
        return categories.getProducts();
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        if (!restaurantRepositories.existsById(restaurant.getId())) {
            throw new EntityNotFoundException("restaurant not found!");
        }
        return restaurantRepositories.save(restaurant);
    }

    public Categories updateCategory(Categories categories) {
        if (!categoriesRepositories.existsById(categories.getId())) {
            throw new EntityNotFoundException("Category not found!");
        }
        return categoriesRepositories.save(categories);
    }

    public Product updateProduct(Product product) {
        if (!productRepositories.existsById(product.getId())) {
            throw new EntityNotFoundException("Product not found!");
        }
        return productRepositories.save(product);
    }

    public void deleteRestaurant(long id) {
        if (!restaurantRepositories.existsById(id)) {
            throw new EntityNotFoundException("restaurant not found!");
        }


        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(id);
        if (optionalRestaurant.isEmpty()) { throw new NullPointerException("Restaurant is not found"); }
        Restaurant restaurant = optionalRestaurant.get();

        if (restaurant.getCategories() != null || !restaurant.getCategories().isEmpty()) {
            for (Categories category : restaurant.getCategories()) {
                for (Product product : category.getProducts()) {
                    product.setDeleted(true);
                    productRepositories.save(product);
                }
                categoriesRepositories.delete(category);
            }
        }

        restaurant.setDeleted(true);
        restaurantRepositories.save(restaurant);
    }

    public void deleteCategory(long id) {

        Optional<Categories> categoriesOptional = categoriesRepositories.findById(id);
        if (categoriesOptional.isEmpty()) {
            throw new EntityNotFoundException("Category not found");
        }
        categoriesRepositories.delete(categoriesOptional.get());
    }

    public void deleteCategories(long[] idList) {

        List<Categories> categories = new ArrayList<>(idList.length+1);

        for (long id : idList) {
            Optional<Categories> categoriesOptional = categoriesRepositories.findById(id);
            if (categoriesOptional.isEmpty()) {
                throw new EntityNotFoundException("not found Category by id: " + id);
            }
            categories.add(categoriesOptional.get());
        }

        categoriesRepositories.deleteAll(categories);
    }

    public void deleteProducts(long[] idList) {

        for (long id : idList) {
            Optional<Product> productOptional = productRepositories.findById(id);
            if (productOptional.isEmpty()) {
                throw new EntityNotFoundException("not found Category by id: " + id);
            }

            Product product = productOptional.get();
            product.setDeleted(true);

            productRepositories.save(product);
        }

    }



    public void deleteProduct(long id) {
        Optional<Product> productOptional = productRepositories.findById(id);
        if (productOptional.isEmpty()) {
            throw new EntityNotFoundException("Product not found");
        }

        Product product = productOptional.get();
        product.setDeleted(true);
        productRepositories.save(product);
    }

}
