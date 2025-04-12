package ru.diplom.fpd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.fpd.model.*;
import ru.diplom.fpd.repositories.CategoriesRepositories;
import ru.diplom.fpd.repositories.ProductRepositories;
import ru.diplom.fpd.repositories.RestaurantAddressRepositories;
import ru.diplom.fpd.repositories.RestaurantRepositories;

import jakarta.persistence.EntityNotFoundException;
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
        return restaurantRepositories.findByIsDeletedFalseAndPublishedTrue();
    }

    public List<Restaurant> getAllRestaurant() {
        return restaurantRepositories.findAll();
    }

    public Restaurant getRestaurant(long id) {

        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(id);
        if (optionalRestaurant.isEmpty()) { throw new EntityNotFoundException("Restaurant is not found"); }
        return optionalRestaurant.get();
    }


    public Restaurant getRestaurantByName(String name) {
        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findByNormalizedNameAndPublishedTrueAndIsDeletedFalse(name.toUpperCase(Locale.ROOT));

        if (optionalRestaurant.isEmpty()) {
            throw new EntityNotFoundException("restaurant not found");
        }

        return optionalRestaurant.get();
    }

    public Restaurant getRestaurantByNameAndCity(String name, String city) {

        Optional<Restaurant> optionalRestaurant =
                restaurantRepositories.findByNormalizedNameAndPublishedTrueAndCitiesAddressInAndIsDeletedFalse(
                        name.toUpperCase(Locale.ROOT),
                        addressRepositories.findAllByCity_NormalizedCiti(city.toUpperCase(Locale.ROOT))
                );

        if (optionalRestaurant.isEmpty()) {
            throw new EntityNotFoundException("restaurant not found");
        }

        return optionalRestaurant.get();
    }

    public List<Restaurant> getRestaurantsByCategoryName(String name) {

        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Category name not set");
        }

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Categories category : categoriesRepositories.findByNormalizedName(name.toUpperCase(Locale.ROOT))) {

            Optional<Restaurant> optionalRestaurant = restaurantRepositories.findAllByCategoriesContainingAndPublishedTrueAndIsDeletedFalse(category);
            if (optionalRestaurant.isEmpty()) {
                continue;
                //throw new EntityNotFoundException("Restaurant is not found");
            }

            restaurantList.add(optionalRestaurant.get());
        }

        return restaurantList;
    }

    public List<Restaurant> getRestaurantsByCategoryNameAndCity(String name, String city) {

        if (name == null || name.isEmpty()) {
            throw new NullPointerException("Category name not set");
        }

        Cities cities = citiesServices.getByName(city);

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Categories categories : categoriesRepositories.findByNormalizedName(name.toUpperCase(Locale.ROOT))) {
            Restaurant restaurant = categories.getRestaurant();
            if (restaurant.isPublished() && !restaurant.isDeleted()) {
                for (RestaurantAddress citiesAddress : restaurant.getCitiesAddress()) {
                    if (citiesAddress.getCity().equals(cities)) {
                       // Hibernate.initialize(list);
                        restaurant.getDescription();
                        restaurantList.add(restaurant);

                        break;
                    }
                }
            }
        }



//        for (Categories category : categoriesRepositories.findByNormalizedName(name.toUpperCase(Locale.ROOT))) {
//
//            Optional<Restaurant> optionalRestaurant = restaurantRepositories.findAllByCategoriesContainingAndPublishedTrueAndCitiesAddressIn(
//                    category,
//                    addressRepositories.findAllByCity_NormalizedCiti(city.toUpperCase(Locale.ROOT))
//            );
//            if (optionalRestaurant.isEmpty()) {
//                continue;
//                //throw new EntityNotFoundException("Restaurant is not found");
//            }
//
//            restaurantList.add(optionalRestaurant.get());
//        }

        return restaurantList;
    }

    public List<Restaurant> getRestaurantsByProductName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("product name not set");
        }

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Product product : productRepositories.findByNormalizedNameAndIsDeletedFalse(name.toUpperCase(Locale.ROOT).trim())) {

            Optional<Categories> optionalCategory = categoriesRepositories.findByProductsContaining(product);
            if (optionalCategory.isEmpty()) {
                continue;
            }

            Optional<Restaurant> optionalRestaurant = restaurantRepositories.findAllByCategoriesContainingAndPublishedTrueAndIsDeletedFalse(optionalCategory.get());
            if (optionalRestaurant.isEmpty()) {
                continue;
            }

            restaurantList.add(optionalRestaurant.get());
        }

        return restaurantList;
    }

    public List<Restaurant> getRestaurantsByProductNameAndCity(String name, String city) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("product name not set");
        }


        Cities cities = citiesServices.getByName(city);

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Product product : productRepositories.findByNormalizedNameAndIsDeletedFalse(name.toUpperCase(Locale.ROOT).trim())) {
            Restaurant restaurant = product.getCategory().getRestaurant();
            if (restaurant.isPublished() && !restaurant.isDeleted()) {
                for (RestaurantAddress citiesAddress : restaurant.getCitiesAddress()) {
                    if (citiesAddress.getCity().equals(cities)) {
                        restaurantList.add(product.getCategory().getRestaurant());
                        break;
                    }
                }
            }
        }

//        for (Product product : productRepositories.findByNormalizedNameAndIsDeletedFalse(name.toUpperCase(Locale.ROOT).trim())) {
//
//            Optional<Categories> optionalCategory = categoriesRepositories.findByProductsContaining(product);
//            if (optionalCategory.isEmpty()) {
//                continue;
//            }
//
//            Optional<Restaurant> optionalRestaurant = restaurantRepositories.findAllByCategoriesContainingAndPublishedTrueAndCitiesAddressIn(
//                    optionalCategory.get(),
//                    addressRepositories.findAllByCity_NormalizedCiti(city.toUpperCase(Locale.ROOT)));
//            if (optionalRestaurant.isEmpty()) {
//                continue;
//            }
//
//            restaurantList.add(optionalRestaurant.get());
//        }

        return restaurantList;
    }

    public List<Restaurant> getRestaurantsByCityName(String name) {

        if (name == null || name.isEmpty()) {
            throw new NullPointerException("city not set");
        }

        return restaurantRepositories.findAllByCitiesAddressInAndPublishedTrueAndIsDeletedFalse(
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

    public boolean exist(long id) {
        return restaurantRepositories.existsByIdAndPublishedTrueAndIsDeletedFalse(id);
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

    public void setPublish(long id, boolean value) {
        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(id);

        if (optionalRestaurant.isEmpty()) {
            throw new EntityNotFoundException("restaurant not found");
        }

        Restaurant restaurant = optionalRestaurant.get();
        restaurant.setPublished(value);
        restaurantRepositories.save(restaurant);
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {

        if (restaurant == null) {
            throw new NullPointerException("restaurant not set");
        }

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
                category.setDeleted(true);
                categoriesRepositories.save(category);
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
        Categories category = categoriesOptional.get();
        category.setDeleted(true);
        categoriesRepositories.save(category);
    }

    public void deleteCategories(long[] idList) {

        List<Categories> categories = new ArrayList<>(idList.length+1);

        for (long id : idList) {
            Optional<Categories> categoriesOptional = categoriesRepositories.findById(id);
            if (categoriesOptional.isEmpty()) {
                throw new EntityNotFoundException("not found Category by id: " + id);
            }
            Categories category = categoriesOptional.get();
            category.setDeleted(true);
            categories.add(category);
        }

        categoriesRepositories.saveAll(categories);
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
