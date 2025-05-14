package ru.diplom.fpd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.diplom.fpd.dto.CategoriesDto;
import ru.diplom.fpd.dto.CategoriesUpdateDto;
import ru.diplom.fpd.dto.ProductDto;
import ru.diplom.fpd.dto.RestaurantDto;
import ru.diplom.fpd.dto.filter.RestaurantFilterDto;
import ru.diplom.fpd.dto.requestModel.CreateRestaurantDto;
import ru.diplom.fpd.mapper.RestaurantMapper;
import ru.diplom.fpd.model.Categories;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.service.RestaurantService;

@RestController
@RequestMapping("/restaurant")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class RestaurantController {
    private final RestaurantMapper restaurantMapper;

    private final RestaurantService restaurantService;

    @Operation(summary = "Получить данные ресторана", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @Operation(summary = "Получить список ресторанов по фильтрам", parameters = {
            @Parameter(name = "filters", description = "Фильтры", in = ParameterIn.QUERY)
    })
    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getRestaurants(@ParameterObject RestaurantFilterDto filters) {
        return ResponseEntity.ok(restaurantService.getRestaurantList(filters));
    }

    @Operation(summary = "Получить категории ресторана", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoriesDto>> getRestaurantCategories(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantCategories(id));
    }

    @Operation(summary = "Получить данные категории", parameters = {
            @Parameter(name = "id", description = "Идетификатор категории", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoriesDto> getCategory(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getCategories(id));
    }

    @Operation(summary = "Получить списко всех категорий")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoriesDto>> getCategoryList() {
        return ResponseEntity.ok(restaurantService.getCategoriesList());
    }

    @Operation(summary = "Получить данные продукта", parameters = {
            @Parameter(name = "id", description = "Идетификатор продукта", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {
        return ResponseEntity.ok(restaurantService.getProduct(id));
    }

    //todo переделать логику поиска
    @Operation(summary = "Поиск по рестаранам, категориям и продуктам", parameters = {
            @Parameter(name = "value", description = "Строка поиска", in = ParameterIn.PATH, required = true),
            @Parameter(name = "city", description = "Город", in = ParameterIn.PATH, required = true)
    })
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

    @Operation(summary = "Создать ресторан")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PostMapping()
    public ResponseEntity<RestaurantDto> addRestaurant(@RequestBody CreateRestaurantDto restaurantDto) {

        return ResponseEntity.ok(restaurantMapper.toDto(restaurantService.add(restaurantDto)));
    }

    @Operation(summary = "Создать категорию", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PostMapping("/{id}/category")
    public ResponseEntity<List<CategoriesDto>> addCategory(@RequestBody CategoriesDto categories, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addCategory(id, categories));
    }

    //todo не думаю что этот рест будет нужен
    @PostMapping("/{id}/categories")
    public ResponseEntity<List<CategoriesDto>> addCategories(@RequestBody List<Categories> categories, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addCategoryList(id, categories));
    }

    @Operation(summary = "Создать продукт", parameters = {
            @Parameter(name = "id", description = "Идетификатор категории", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PostMapping("/categories/{id}/product")
    public ResponseEntity<List<ProductDto>> addProduct(@RequestBody ProductDto product, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addProduct(id, product));
    }

    @Operation(summary = "Создать список продуктов", parameters = {
            @Parameter(name = "id", description = "Идетификатор категории", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PostMapping("/categories/{id}/products")
    public ResponseEntity<List<ProductDto>> addProducts(@RequestBody List<ProductDto> products, @PathVariable long id) {
        return ResponseEntity.ok(restaurantService.addProductList(id, products));
    }

    @Operation(summary = "Опубликовать ресторан", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PutMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable long id) {
        restaurantService.setPublish(id, true);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Скрыть ресторан", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PutMapping("/{id}/hide")
    public ResponseEntity<Void> hide(@PathVariable long id) {
        restaurantService.setPublish(id, false);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить данные ресторана")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PutMapping()
    public ResponseEntity<RestaurantDto> updateRestaurant(@RequestBody CreateRestaurantDto restaurant) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant));
    }

    @Operation(summary = "Изменить данные категории")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PutMapping("/category")
    public ResponseEntity<CategoriesDto> updateCategory(@RequestBody CategoriesUpdateDto categories) {
        return ResponseEntity.ok(restaurantService.updateCategory(categories));
    }

    @Operation(summary = "Изменить данные проддукта")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @PutMapping("/product")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto product) {
        return ResponseEntity.ok(restaurantService.updateProduct(product));
    }

    @Operation(summary = "Удалить ресторан", parameters = {
            @Parameter(name = "id", description = "Идетификатор ресторана", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Удалить категорию", parameters = {
            @Parameter(name = "id", description = "Идетификатор категории", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long id) {
        restaurantService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удолить список категорий", requestBody =
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Список идентификаторов категорий"
    )
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @DeleteMapping("/categories")
    public ResponseEntity<Void> deleteCategoryList(@RequestBody long[] id) {
        restaurantService.deleteCategories(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить продукт", parameters = {
            @Parameter(name = "id", description = "Идетификатор продукта", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        restaurantService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удолить список продуктов", requestBody =
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Список идентификаторов продуктов"
    ))
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_RESTAURANT_ADMIN'))")
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
        map.put("Status", String.valueOf(status.value()));
        map.put("Error", status.getReasonPhrase());
        map.put("Message", ex.getMessage());
        map.put("Path", request.getContextPath());
        return ResponseEntity.status(status).body(map);
    }

}
