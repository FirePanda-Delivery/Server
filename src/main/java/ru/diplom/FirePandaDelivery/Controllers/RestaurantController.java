package ru.diplom.FirePandaDelivery.Controllers;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import ru.diplom.FirePandaDelivery.dto.requestModel.RestaurantReq;
import ru.diplom.FirePandaDelivery.dto.requestModel.UpdateRestaurantReq;
import ru.diplom.FirePandaDelivery.model.*;
import ru.diplom.FirePandaDelivery.service.CitiesServices;
import ru.diplom.FirePandaDelivery.service.OrderServices;
import ru.diplom.FirePandaDelivery.service.RestaurantService;
import ru.diplom.FirePandaDelivery.dto.responseModel.RestaurantResp;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final OrderServices orderServices;
    private final CitiesServices citiesServices;
    private final Log logger =  LogFactory.getLog(getClass());

    @Autowired
    public RestaurantController(RestaurantService restaurantService, OrderServices orderServices, CitiesServices citiesServices) {
        this.restaurantService = restaurantService;
        this.orderServices = orderServices;
        this.citiesServices = citiesServices;
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

    @GetMapping("/only/{id}")
    public ResponseEntity<RestaurantResp> getOnlyRestaurant(@PathVariable long id) {

        return ResponseEntity.ok(RestaurantResp.toRestaurantResponse(restaurantService.getRestaurant(id)));
    }


    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Categories>> getRestaurantCategories(@PathVariable long id) {

        return ResponseEntity.ok(restaurantService.getRestaurant(id).getCategories().stream().filter(item -> {
            if (!item.isDeleted()){
                item.setProducts(item.getProducts().stream().filter(product -> !product.isDeleted())
                       .collect(Collectors.toList()));
                return true;
            }
            return false;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/exist/{id}")
    public ResponseEntity<Object> existRestaurant(@PathVariable long id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("value", restaurantService.exist(id));
        return ResponseEntity.ok(map);
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
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody RestaurantReq restaurantReq) {
        Restaurant restaurant = new Restaurant();

        restaurant.setName(restaurantReq.getName());
        restaurant.setDescription(restaurantReq.getDescription());
        restaurant.setMinPrice(restaurantReq.getMinPrice());
        restaurant.setOwnDelivery(restaurantReq.isOwnDelivery());
        restaurant.setWorkingHoursEnd(restaurantReq.getWorkingHoursEnd());
        restaurant.setWorkingHoursStart(restaurantReq.getWorkingHoursStart());
        restaurant.setCategories(new LinkedList<>());

        List<RestaurantAddress> cityAddress = new LinkedList<>();

        for (Map<String, String> address : restaurantReq.getCitiesAddress()) {
            RestaurantAddress restaurantAddress = new RestaurantAddress();

            restaurantAddress.setCity(citiesServices.getByName(address.get("city")));
            restaurantAddress.setAddress(address.get("address"));

            cityAddress.add(restaurantAddress);
        }

        restaurant.setCitiesAddress(cityAddress);

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

    @PutMapping("/{id}/publish")
    public ResponseEntity<Restaurant> publish(@PathVariable long id) {
        restaurantService.setPublish(id, true);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/hide")
    public ResponseEntity<Restaurant> hide(@PathVariable long id) {
        restaurantService.setPublish(id, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody UpdateRestaurantReq restaurantReq) {
        Restaurant restaurant = restaurantService.getRestaurant(restaurantReq.getId());
        restaurant.setName(restaurantReq.getName());
        restaurant.setWorkingHoursStart(restaurantReq.getWorkingHoursStart());
        restaurant.setWorkingHoursEnd(restaurantReq.getWorkingHoursEnd());
        restaurant.setOwnDelivery(restaurantReq.isOwnDelivery());
        restaurant.setMinPrice(restaurantReq.getMinPrice());
        restaurant.setDescription(restaurantReq.getDescription());
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

    @ExceptionHandler({ClassCastException.class})
    public ResponseEntity<Object> handleClassCast(ClassCastException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, String> map = new LinkedHashMap<>();
        logger.error(ex + ". " + request.toString() + ". " + Arrays.toString(ex.getStackTrace()));
        map.put("Timestamp", new Date().toString());
        map.put("Status",  String.valueOf(status.value()));
        map.put("Error", status.getReasonPhrase());
        map.put("Message", ex.getMessage());
        map.put("Path", request.getContextPath());
        return ResponseEntity.status(status).body(map);
    }

}
