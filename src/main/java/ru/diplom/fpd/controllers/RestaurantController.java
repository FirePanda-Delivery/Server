package ru.diplom.fpd.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.diplom.fpd.dto.CategoriesDto;
import ru.diplom.fpd.dto.CategoriesUpdateDto;
import ru.diplom.fpd.dto.OrderDto;
import ru.diplom.fpd.dto.ProductDto;
import ru.diplom.fpd.dto.RestaurantDto;
import ru.diplom.fpd.dto.filter.RestaurantFilterDto;
import ru.diplom.fpd.dto.requestModel.CreateRestaurantDto;
import ru.diplom.fpd.mapper.RestaurantMapper;
import ru.diplom.fpd.model.*;
import ru.diplom.fpd.service.CitiesServices;
import ru.diplom.fpd.service.OrderServices;
import ru.diplom.fpd.service.RestaurantService;

import java.util.*;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
@Slf4j
public class RestaurantController {
    private final RestaurantMapper restaurantMapper;

    private final RestaurantService restaurantService;
    private final OrderServices orderServices;
    private final CitiesServices citiesServices;


    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @GetMapping(params = {"city"})
    public ResponseEntity<List<RestaurantDto>> getRestaurants(@RequestParam RestaurantFilterDto filters) {
        return ResponseEntity.ok(restaurantService.getRestaurantList(filters));
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoriesDto>> getRestaurantCategories(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantCategories(id));
    }

//    @GetMapping("/exist/{id}")
//    public ResponseEntity<Object> existRestaurant(@PathVariable long id) {
//        Map<String, Boolean> map = new HashMap<>();
//        map.put("value", restaurantService.exist(id));
//        return ResponseEntity.ok(map);
//    }

    @GetMapping("/{id}/order/active")
    public ResponseEntity<List<OrderDto>> getActiveOrder(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getActiveRestaurantOrder(id));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoriesDto> getCategory(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getCategories(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoriesDto>> getCategoryList() {
        return ResponseEntity.ok(restaurantService.getCategoriesList());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getProduct(id));
    }

//    @GetMapping("/product")
//    public ResponseEntity<List<Product>> getProductList() {
//        return ResponseEntity.ok(restaurantService.getProductList());
//    }

    @GetMapping(value = "/search", params = {"value", "city"})
    public ResponseEntity<List<Restaurant>> search(String value, String city) {

        try {
            Restaurant restaurant = restaurantService.getRestaurantByNameAndCity(value, city);
            return ResponseEntity.ok(Collections.singletonList(restaurant));
        } catch (Exception ignored) {

        }

        List<Restaurant> list = restaurantService.getRestaurantsByCategoryNameAndCity(value, city);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        }

        list = restaurantService.getRestaurantsByProductNameAndCity(value, city);

        if (list != null && !list.isEmpty()) {
            return ResponseEntity.ok(list);
        }

        return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping()
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody CreateRestaurantDto restaurantDto) {

        return ResponseEntity.ok(restaurantMapper.toDto(restaurantService.add(restaurantDto)));
    }

    @PostMapping("/{id}/category")
    public ResponseEntity<List<CategoriesDto>> addCategory(@RequestBody CategoriesDto categories, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addCategory(id, categories));
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<List<CategoriesDto>> addCategories(@RequestBody List<Categories> categories, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addCategoryList(id, categories));
    }

    @PostMapping("/categories/{id}/product")
    public ResponseEntity<List<ProductDto>> addProduct(@RequestBody ProductDto product, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addProduct(id, product));
    }

    @PostMapping("/categories/{id}/products")
    public ResponseEntity<List<ProductDto>> addProducts(@RequestBody List<ProductDto> products, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addProductList(id, products));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable long id) {
        restaurantService.setPublish(id, true);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/hide")
    public ResponseEntity<Void> hide(@PathVariable long id) {
        restaurantService.setPublish(id, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    public ResponseEntity<RestaurantDto> updateRestaurant(@RequestBody CreateRestaurantDto restaurant) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant));
    }

    @PutMapping("/category")
    public ResponseEntity<CategoriesDto> updateCategory(@RequestBody CategoriesUpdateDto categories) {
        return ResponseEntity.ok(restaurantService.updateCategory(categories));
    }

    @PutMapping("/product")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto product) {
        return ResponseEntity.ok(restaurantService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        restaurantService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/categories")
    public ResponseEntity<Void> deleteCategoryList(@RequestBody long[] id) {
        restaurantService.deleteCategories(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        restaurantService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products")
    public ResponseEntity<Void> deleteProductList(@RequestBody long[] id) {
        restaurantService.deleteCategories(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({ClassCastException.class})
    public ResponseEntity<Object> handleClassCast(ClassCastException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new LinkedHashMap<>();
        log.error(ex + ". " + request.toString() + ". " + Arrays.toString(ex.getStackTrace()));
        map.put("Timestamp", new Date().toString());
        map.put("Status",  String.valueOf(status.value()));
        map.put("Error", status.getReasonPhrase());
        map.put("Message", ex.getMessage());
        map.put("Path", request.getContextPath());
        return ResponseEntity.status(status).body(map);
    }

}
