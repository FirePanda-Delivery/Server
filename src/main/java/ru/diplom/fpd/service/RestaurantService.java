package ru.diplom.fpd.service;

import com.ibm.icu.impl.Pair;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.diplom.fpd.dto.AverageDeliveryTimeDto;
import ru.diplom.fpd.dto.CategoriesDto;
import ru.diplom.fpd.dto.CategoriesUpdateDto;
import ru.diplom.fpd.dto.PandaPage;
import ru.diplom.fpd.dto.ProductDto;
import ru.diplom.fpd.dto.RestaurantDto;
import ru.diplom.fpd.dto.filter.RestaurantFilterDto;
import ru.diplom.fpd.dto.requestModel.CreateRestaurantDto;
import ru.diplom.fpd.mapper.AverageDeliveryTimeMapper;
import ru.diplom.fpd.mapper.CategoriesMapper;
import ru.diplom.fpd.mapper.ProductMapper;
import ru.diplom.fpd.mapper.RestaurantMapper;
import ru.diplom.fpd.model.AverageDeliveryTime;
import ru.diplom.fpd.model.Categories;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.Product;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;
import ru.diplom.fpd.repositories.AverageDeliveryTimeRepository;
import ru.diplom.fpd.repositories.CategoriesRepositories;
import ru.diplom.fpd.repositories.ProductRepositories;
import ru.diplom.fpd.repositories.RestaurantAddressRepositories;
import ru.diplom.fpd.repositories.RestaurantRepositories;

@AllArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepositories restaurantRepositories;
    private final CategoriesRepositories categoriesRepositories;
    private final ProductRepositories productRepositories;
    private final CitiesServices citiesServices;
    private final RestaurantAddressRepositories addressRepositories;
    private final RestaurantMapper restaurantMapper;
    private final CategoriesMapper categoriesMapper;
    private final ProductMapper productMapper;
    private final AverageDeliveryTimeRepository averageDeliveryTimeRepository;
    private final AverageDeliveryTimeMapper averageDeliveryTimeMapper;

    public List<Restaurant> getRestaurantList() {
        return restaurantRepositories.findByIsDeletedFalseAndPublishedTrue();
    }

    public List<Restaurant> getAllRestaurant() {
        return restaurantRepositories.findAll();
    }

    public RestaurantDto getRestaurant(long id) {
        return restaurantRepositories.findById(id)
                .map(restaurantMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Restaurant getRestaurantEntity(long id) {
        return restaurantRepositories.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }


    public Restaurant getRestaurantByName(String name) {
        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findByNameIgnoreCaseAndPublishedTrueAndIsDeletedFalse(name.toUpperCase(Locale.ROOT));

        if (optionalRestaurant.isEmpty()) {
            throw new EntityNotFoundException("restaurant not found");
        }

        return optionalRestaurant.get();
    }

    public Restaurant getRestaurantByNameAndCity(String name, String city) {

        Optional<Restaurant> optionalRestaurant =
                restaurantRepositories.findByNameIgnoreCaseAndPublishedTrueAndCitiesAddressInAndIsDeletedFalse(
                        name.toUpperCase(Locale.ROOT),
                        addressRepositories.findAllByCityIgnoreCase(city)
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

        for (Categories category : categoriesRepositories.findByNameIgnoreCase(name.toUpperCase(Locale.ROOT))) {

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

        City cities = citiesServices.getEntityByName(city);

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Categories categories : categoriesRepositories.findByNameIgnoreCase(name.toUpperCase(Locale.ROOT))) {
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

        for (Product product : productRepositories.findByNameIgnoreCaseAndIsDeletedFalse(name.trim())) {

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


        City cities = citiesServices.getEntityByName(city);

        List<Restaurant> restaurantList = new LinkedList<>();

        for (Product product : productRepositories.findByNameIgnoreCaseAndIsDeletedFalse(name.trim())) {
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

    public PandaPage<RestaurantDto> getRestaurantList(Pageable pageable, RestaurantFilterDto filter) {
        Page<RestaurantDto> page = restaurantRepositories.findAll((root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    Optional.ofNullable(filter.getCity()).ifPresent(city ->
                            predicates.add(cb.equal(root.join("citiesAddress").get("city").get("city"), city)));
                    Optional.ofNullable(filter.getCategories()).ifPresent(categories ->
                            predicates.add(root.join("categories").get("name").in(categories)));
                    Optional.ofNullable(filter.getProducts()).ifPresent(products ->
                            predicates.add(root.join("categories").join("products").get("name").in(products)));
                    return cb.and(predicates.toArray(Predicate[]::new));
                }, pageable).map(restaurantMapper::toDto);
        return PandaPage.of(page);
    }

    public List<CategoriesDto> getRestaurantCategories(long restaurantId) {

        return restaurantRepositories.findById(restaurantId)
                .map(Restaurant::getCategories)
                .stream()
                .flatMap(List::stream)
                .map(categoriesMapper::toDto)
                .toList();
    }

    public List<Categories> getAllCategories() {

        return categoriesRepositories.findAll();
    }

    public List<CategoriesDto> getCategoriesList() {
        return categoriesRepositories.findByIsDeletedFalse().stream()
                .map(categoriesMapper::toDto)
                .toList();
    }

    public CategoriesDto getCategories(long id) {
        return categoriesRepositories.findById(id)
                .map(categoriesMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Product> getProductList() {
        return productRepositories.findByIsDeletedFalse();
    }

    public List<Product> getAllProduct() {
        return productRepositories.findAll();
    }

    public ProductDto getProduct(long id) {
        return productRepositories.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Product getProductEntity(long id) {
        return productRepositories.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Restaurant add(CreateRestaurantDto restaurantDto) {
        Restaurant restaurant = restaurantMapper.creteDtoToEntity(restaurantDto);
        restaurant.setCategories(new LinkedList<>());

        restaurant.setCitiesAddress(restaurantDto
                .getCitiesAddress()
                .stream()
                .map(dto -> RestaurantAddress.builder()
                        .address(dto.getAddress())
                        .city(citiesServices.getEntityByName(dto.getCity()))
                        .restaurant(restaurant)
                        .build())
                .toList());
        return restaurantRepositories.save(restaurant);
    }


    public List<CategoriesDto> addCategory(long restId, CategoriesDto categoryDto) {
        if (categoryDto == null) {
            throw new NullPointerException("category not set");
        }

        if (!restaurantRepositories.existsById(restId)) {
            throw new EntityNotFoundException("Restaurant is not found");
        }

        Restaurant restaurant = restaurantRepositories.findById(restId).orElseThrow(EntityNotFoundException::new);
        Categories category = categoriesMapper.toEntity(categoryDto);
        category.setRestaurant(restaurant);
        categoriesRepositories.save(category);

        return restaurant.getCategories().stream()
                .map(categoriesMapper::toDto)
                .toList();
    }


    public List<CategoriesDto> addCategoryList(long restId, List<Categories> categories) {

        if (categories == null) {
            throw new NullPointerException("categories not set");
        }

        if (categories.isEmpty()) {
            throw new NullPointerException("categories is empty");
        }

        if (!restaurantRepositories.existsById(restId)) {
            throw new EntityNotFoundException("Restaurant is not found");
        }

        Restaurant restaurant = restaurantRepositories.findById(restId).orElseThrow(EntityNotFoundException::new);
        restaurant.getCategories().addAll(categories);
        restaurant = restaurantRepositories.save(restaurant);
        return restaurant.getCategories().stream()
                .map(categoriesMapper::toDto)
                .toList();
    }

    public List<ProductDto> addProduct(long catId, ProductDto product) {
        if (product == null) {
            throw new NullPointerException("products not set");
        }

        if (!categoriesRepositories.existsById(catId)) {
            throw new EntityNotFoundException("Categories is not found");
        }

        Categories category = categoriesRepositories.findById(catId).orElseThrow(EntityNotFoundException::new);
        category.getProducts().add(productMapper.toEntity(product));
        category = categoriesRepositories.save(category);
        return category.getProducts().stream()
                .map(productMapper::toDto)
                .toList();
    }

    public List<ProductDto> addProductList(long catId, List<ProductDto> products) {

        if (products == null) {
            throw new NullPointerException("products not set");
        }

        if (products.isEmpty()) {
            throw new NullPointerException("products is empty");
        }

        if (!categoriesRepositories.existsById(catId)) {
            throw new EntityNotFoundException("Categories is not found");
        }


        Categories categories = categoriesRepositories.findById(catId).orElseThrow(EntityNotFoundException::new);
        categories.getProducts().addAll(products.stream().map(productMapper::toEntity).toList());
        categories = categoriesRepositories.save(categories);
        return categories.getProducts().stream()
                .map(productMapper::toDto)
                .toList();
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

    @Transactional
    public RestaurantDto updateRestaurant(@NonNull CreateRestaurantDto restaurantDto) {

        Restaurant restaurant = getRestaurantEntity(restaurantDto.getId());
        restaurantMapper.updateEntity(restaurant, restaurantDto);
        List<RestaurantAddress> restaurantAddresses = restaurantDto.getCitiesAddress().stream()
                .map(address -> Pair.of(citiesServices.getEntityByName(address.getCity()), address))
                .map(pair ->
                        addressRepositories.findByCityAndAddress(pair.first, pair.second.getAddress(), restaurant.getId())
                                .orElseGet(() -> RestaurantAddress.builder()
                                        .address(pair.second.getAddress())
                                        .city(pair.first)
                                        .restaurant(restaurant)
                                        .build()))
                .collect(Collectors.toList());
        restaurant.setCitiesAddress(restaurantAddresses);
        return restaurantMapper.toDto(restaurantRepositories.save(restaurant));
    }

    public CategoriesDto updateCategory(CategoriesUpdateDto categoryDto) {

        Categories category = categoriesRepositories.findById(categoryDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        categoriesMapper.updateCategory(category, categoryDto);
        return categoriesMapper.toDto(categoriesRepositories.save(category));
    }

    public ProductDto updateProduct(ProductDto productdto) {
        Product product = productRepositories.findById(productdto.getId())
                .orElseThrow(EntityNotFoundException::new);

        productMapper.updateProduct(product, productdto);
        return productMapper.toDto(productRepositories.save(product));
    }

    public void deleteRestaurant(long id) {
        if (!restaurantRepositories.existsById(id)) {
            throw new EntityNotFoundException("restaurant not found!");
        }


        Optional<Restaurant> optionalRestaurant = restaurantRepositories.findById(id);
        if (optionalRestaurant.isEmpty()) {
            throw new NullPointerException("Restaurant is not found");
        }
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

        List<Categories> categories = new ArrayList<>(idList.length + 1);

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

    @Transactional
    public void updateRestaurantDeliveryTime(List<AverageDeliveryTimeDto> message) {
        List<AverageDeliveryTime> list = message.stream()
            .map(dto -> averageDeliveryTimeRepository
                .findByRestaurant_Id(dto.getRestaurantId())
                .map(deliveryTime -> averageDeliveryTimeMapper.partialUpdate(dto, deliveryTime))
                .orElseGet(() -> averageDeliveryTimeMapper.toEntity(dto)))
            .toList();
        averageDeliveryTimeRepository.saveAll(list);
    }
}
