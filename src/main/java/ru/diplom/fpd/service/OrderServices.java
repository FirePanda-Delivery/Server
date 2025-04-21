package ru.diplom.fpd.service;

import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.fpd.dto.ActiveCourier;
import ru.diplom.fpd.dto.OrderDto;
import ru.diplom.fpd.dto.requestModel.CreateOrderDto;
import ru.diplom.fpd.mapper.OrderMapper;
import ru.diplom.fpd.mapper.ProductMapper;
import ru.diplom.fpd.model.*;
import ru.diplom.fpd.processing.AddressProcessing;
import ru.diplom.fpd.repositories.OrderRepositories;
import ru.diplom.fpd.thread.SearchCourierThread;

import jakarta.persistence.EntityNotFoundException;
import java.sql.Time;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderServices {

    private final OrderRepositories orderRepositories;

    private final UserService userService;
    private final CourierService courierService;
    private final RestaurantService restaurantService;
    private final AddressProcessing addressProcessing;
    private final CitiesServices citiesServices;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    private final List<OrderStatus> FINAL_STATUSES = Arrays.asList(OrderStatus.DELIVERED, OrderStatus.CANCELED);


    public List<Order> getOrderList() {
        return orderRepositories.findAll();
    }

    public OrderDto getOrder(long id) {
        return orderMapper.toDto(orderRepositories.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public List<OrderDto> getUserOrders(long userId) {
        return orderRepositories.findAllByUser_Id(userId).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public List<OrderDto> getCourierOrders(long id) {
        return orderRepositories.findAllByCourierId(id).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public Order getActiveCourierOrder(long courierId) {

        Order order = Storage.courierActiveOrder.get(courierId);

        if (order != null) {
            return order;
        }

        Optional<Order> orderOptional = orderRepositories.findByCourier_IdAndOrderStatusIsNotIn(courierId, FINAL_STATUSES);

        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        order = orderOptional.get();

        Storage.courierActiveOrder.put(courierId, order);

        return order;
    }

    public List<OrderDto> getActiveRestaurantOrder(long restaurantId) {

        List<Order> orderList = Storage.restaurantActiveOrder.get(restaurantId);

        if (orderList == null || orderList.isEmpty()) {

            List<Order> orders = orderRepositories.findAllByRestaurant_IdAndOrderStatusIsNotIn(restaurantId, FINAL_STATUSES);

            Storage.addRestaurantActiveOrderList(restaurantId, orders);

            return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
        }

        return orderList.stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    public List<OrderDto> getRestaurantOrders(long id) {
        return orderRepositories.findAllByRestaurant_Id(id).stream()
                .map(orderMapper::toDto)
                .toList();
    }

//    public List<Order> getCityOrders(String city) {
//        return orderRepositories.findAllByCities(citiesServices.getByName(city));
//    }
//
//    public List<Order> getOrdersByStatus(OrderStatus status) {
//        return orderRepositories.findAllByOrderStatus(status);
//    }
//
    public void addCourier(Order order, Courier courier) {

        if (order == null) {
            throw new NullPointerException("order not set");
        }

        if (courier == null) {
            throw new NullPointerException("courier not set");
        }

        Storage.activeOrder.get(Storage.activeOrder.indexOf(order)).setCourier(courier);

        List<Order> orderList = Storage.restaurantActiveOrder.get(order.getRestaurant().getId());
        orderList.get(orderList.indexOf(order)).setCourier(courier);

        order.setCourier(courier);

        Storage.courierActiveOrder.put(courier.getId(), order);

        orderRepositories.save(order);

        courierService.courierReceivedOrder(courier);
    }

    public OrderDto createOrder(CreateOrderDto createOrderDto) {

        if (createOrderDto.getProducts().isEmpty()) {
            throw new NullPointerException("products not set");
        }
//
//        if (createOrderDto.getAddress() == null || address.isEmpty() || address.equals(" ")) {
//            throw new NullPointerException("address not set");
//        }
//
//        if (city == null || city.isEmpty() || city.equals(" ")) {
//            throw new NullPointerException("city not set");
//        }

        Set<OrderProduct> orderProducts = createOrderDto.getProducts().stream()
                .map(productMapper::toOrderEntity)
                .collect(Collectors.toSet());

        Restaurant restaurant = restaurantService.getRestaurantEntity(createOrderDto.getRestaurantId());
        String city = createOrderDto.getCity();
        String address = createOrderDto.getAddress();


        RestaurantAddress restaurantAddress = addressProcessing.restaurantNearestToAddress(restaurant, city, address);


        Order order = Order.builder()
                .address(address)
                .user(userService.get(createOrderDto.getUserId()))
                .restaurant(restaurant)
                .orderStatus(OrderStatus.CREATED)
                .date(new Date())
                .productList(orderProducts)
                .timeStart(new Time(new Date().getTime()))
                .restaurantAddress(restaurantAddress)
                .cities(citiesServices.getByName(city))
                .build();

        List<ActiveCourier> activeCourierList = courierService.getActiveCourierByCity(city);

        if (activeCourierList == null || activeCourierList.isEmpty()) {
            Thread thread = new SearchCourierThread(
                    order,
                    citiesServices.getByName(city),
                    addressProcessing,
                    courierService,
                    this);

            thread.start();


        } else {
            Courier courier = addressProcessing.courierNearestToAddress(activeCourierList,
                    restaurantAddress.getAddress());
            order.setCourier(courier);
            courierService.courierReceivedOrder(courier);
            Storage.courierActiveOrder.put(courier.getId(), order);
        }

        Storage.activeOrder.add(order);
        Storage.addRestaurantActiveOrder(createOrderDto.getRestaurantId(), order);

        return orderMapper.toDto(orderRepositories.save(order));
    }

    public OrderDto setStatus(long id, OrderStatus status){

        if (status == null) {
            throw new NullPointerException("status not set");
        }

        if (status == OrderStatus.DELIVERED) {
            return completeOrder(id);
        }

        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        Order order = orderOptional.get();
        order.setOrderStatus(status);
        return orderMapper.toDto(orderRepositories.save(order));
    }

    public OrderDto completeOrder(long id) {
        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        Order order = orderOptional.get();


        Storage.courierActiveOrder.remove(order.getCourier().getId(), order);
        Storage.restaurantActiveOrder.get(order.getRestaurant().getId()).remove(order);
       // Storage.restaurantActiveOrder.remove(order.getRestaurant().getId(), order);
        Storage.activeOrder.remove(order);

        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setTimeEnd(new Time(new Date().getTime()));

        courierService.courierCompletedOrder(order.getCourier());

        return orderMapper.toDto(orderRepositories.save(order));
    }

    private static class Storage {

        /**
         * stores data about active orders and assigned couriers
         * param Long - courier id
         * param Order - order
         */
        private final static Map<Long, Order> courierActiveOrder = new LinkedHashMap<>();

        private final static Map<Long, List<Order>> restaurantActiveOrder = new LinkedHashMap<>();

        private final static List<Order> activeOrder = new LinkedList<>();

        public static void addRestaurantActiveOrder(long restaurantId, Order order) {
            if (restaurantActiveOrder.get(restaurantId) == null ) {

                List<Order> orderList = new LinkedList<>();
                orderList.add(order);
                restaurantActiveOrder.put(restaurantId, orderList);
            } else {
                restaurantActiveOrder.get(restaurantId).add(order);
            }
        }

        public static void addRestaurantActiveOrderList(long restaurantId, List<Order> order) {
            if (restaurantActiveOrder.get(restaurantId) == null ) {
                restaurantActiveOrder.put(restaurantId, order);
            } else {
                restaurantActiveOrder.get(restaurantId).addAll(order);
            }
        }
    }







}
