package ru.diplom.FirePandaDelivery.Controllers;

import io.swagger.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.Service.RestaurantService;
import ru.diplom.FirePandaDelivery.dto.responseModel.RestaurantResp;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Product;
import ru.diplom.FirePandaDelivery.model.Restaurant;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getRestaurantList(@CookieValue(name = "address", required = false) String address) {
        if (address.isEmpty()) {
            return ResponseEntity.ok(restaurantService.getRestaurantList());
        }
        String city = address.split(",")[1].trim();
        return ResponseEntity.ok(restaurantService.getRestaurantsByCityName(city));
    }

    @GetMapping("/only")
    public ResponseEntity<List<RestaurantResp>> getOnlyRestaurantList(@CookieValue(name = "address", required = false) String address) {
        if (address.isEmpty()) {

        }

      //TODO разобраться с куки

        return ResponseEntity.ok(
                RestaurantResp.toRestaurantResponse(
                        restaurantService.getRestaurantsByCityName(
                                address.split(",")[1].trim()
                        )
                )
        );
    }


    @GetMapping(value = "/getByCategory", params = {"catName"})
    public ResponseEntity<List<Restaurant>> getRestaurantByCategory(String catName) {
        return ResponseEntity.ok(restaurantService.getRestaurantsByCategoryName(catName));
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
