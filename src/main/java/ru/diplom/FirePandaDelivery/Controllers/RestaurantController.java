package ru.diplom.FirePandaDelivery.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.service.OrderServices;
import ru.diplom.FirePandaDelivery.service.RestaurantService;
import ru.diplom.FirePandaDelivery.dto.responseModel.RestaurantResp;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.model.Product;
import ru.diplom.FirePandaDelivery.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final OrderServices orderServices;

    @Autowired
    public RestaurantController(RestaurantService restaurantService, OrderServices orderServices) {
        this.restaurantService = restaurantService;
        this.orderServices = orderServices;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @GetMapping(params = {"city"})
    public ResponseEntity<List<Restaurant>> getRestaurantList(String city) {
        if (city.isEmpty()) {
            return ResponseEntity.ok(restaurantService.getRestaurantList());
        }
        return ResponseEntity.ok(restaurantService.getRestaurantsByCityName(city));
    }

    @GetMapping(value = "/only", params = {"city"})
    public ResponseEntity<List<RestaurantResp>> getOnlyRestaurantList(String city) {

        if (city.isEmpty()) {
            throw new NullPointerException("address is not set");
        }

        return ResponseEntity.ok(
                RestaurantResp.toRestaurantResponse(
                        restaurantService.getRestaurantsByCityName(city.trim())
                )
        );
    }


    @GetMapping(value = "/getByCategory", params = {"catName"})
    public ResponseEntity<List<Restaurant>> getRestaurantByCategory(String catName) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCategoryName(catName));
    }

    @GetMapping("/{id}/order/active")
    public ResponseEntity<List<Order>> getActiveOrder(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getActiveRestaurantOrder(id));
    }

    @GetMapping(value = "/getByProduct", params = {"productName"})
    public ResponseEntity<Object> getRestaurantByProduct(String productName) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByProductName(productName));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Categories> getCategory(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getCategories(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Categories>> getCategoryList() {
        return ResponseEntity.ok(restaurantService.getCategoriesList());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getProduct(id));
    }

    @GetMapping("/product")
    public ResponseEntity<List<Product>> getProductList() {
        return ResponseEntity.ok(restaurantService.getProductList());
    }

    @PostMapping()
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.add(restaurant));
    }

    @PostMapping("/{id}/category")
    public ResponseEntity<List<Categories>> addCategory(@RequestBody Categories categories, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addCategory(id, categories));
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<List<Categories>> addCategories(@RequestBody List<Categories> categories, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addCategoryList(id, categories));
    }

    @PostMapping("/categories/{id}/product")
    public ResponseEntity<List<Product>> addProduct(@RequestBody Product product, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addProduct(id, product));
    }

    @PostMapping("/categories/{id}/products")
    public ResponseEntity<List<Product>> addProducts(@RequestBody List<Product> products, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addProductList(id, products));
    }

    @PutMapping()
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant));
    }

    @PutMapping("/category")
    public ResponseEntity<Categories> updateCategory(@RequestBody Categories categories) {
        return ResponseEntity.ok(restaurantService.updateCategory(categories));
    }

    @PutMapping("/product")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok(restaurantService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long id) {
        restaurantService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/categories")
    public ResponseEntity<Object> deleteCategoryList(@RequestBody long[] id) {
        restaurantService.deleteCategories(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
        restaurantService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products")
    public ResponseEntity<Object> deleteProductList(@RequestBody long[] id) {
        restaurantService.deleteCategories(id);
        return ResponseEntity.ok().build();
    }

}
