package ru.diplom.FirePandaDelivery.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.*;
import ru.diplom.FirePandaDelivery.processing.AddressProcessing;
import ru.diplom.FirePandaDelivery.repositories.OrderRepositories;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.util.*;

@Service
public class OrderServices {

    private final OrderRepositories orderRepositories;

    private final UserService userService;
    private final CourierService courierService;
    private final RestaurantService restaurantService;
    private final AddressProcessing addressProcessing;

    @Autowired
    public OrderServices(OrderRepositories orderRepositories, UserService userService, CourierService courierService, RestaurantService restaurantService, AddressProcessing addressProcessing) {
        this.orderRepositories = orderRepositories;
        this.userService = userService;
        this.courierService = courierService;
        this.restaurantService = restaurantService;
        this.addressProcessing = addressProcessing;
    }

    public List<Order> getOrderList() {
        return orderRepositories.findAll();
    }

    public Order getOrder(long id) {
        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }
        return orderOptional.get();
    }

    public List<Order> getUserOrders(long userId) {
        return orderRepositories.findAllByUser_Id(userId);
    }

    public List<Order> getCourierOrders(long id) {
        return orderRepositories.findAllByCourierId(id);
    }

    public Order getActiveCourierOrder(Courier courier) {

        if (courier == null) {
            throw new NullPointerException();
        }

        Order order = Storage.courierActiveOrder.get(courier.getId());

        if (order != null) {
            return order;
        }

        List<OrderStatus> statuses = new LinkedList<>();
        statuses.add(OrderStatus.DELIVERED);
        statuses.add(OrderStatus.CANCELED);

        Optional<Order> orderOptional = orderRepositories.findByCourier_IdAndOrderStatusIsNotIn(courier.getId(), statuses);

        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        order = orderOptional.get();

        Storage.courierActiveOrder.put(courier.getId(), order);

        return order;
    }

    public List<Order> getRestaurantOrders(long id) {
        return orderRepositories.findAllByRestaurant_Id(id);
    }

    public List<Order> getCityOrders(long id) {
        throw new RuntimeException("Добавить логику");
       // return orderRepositories.findAllByRestaurant_Id(id);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepositories.findAllByOrderStatus(status);
    }


    public Order createOrder(long userId, long restaurantId, Set<OrderProduct> orderProducts, String address, String city) {

        if (orderProducts.isEmpty()) {
            throw new NullPointerException("products not set");
        }

        if (address == null || address.isEmpty() || address.equals(" ")) {
            throw new NullPointerException("address not set");
        }

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);

        String restaurantAddress = addressProcessing.restaurantNearestToAddress(restaurant, city, address);

        Courier courier = addressProcessing.courierNearestToAddress(
                courierService.getActiveCourierByCity(city), restaurantAddress);


        Order order = Order.builder()
                .address(address)
                .user(userService.get(userId))
                .restaurant(restaurant)
                .orderStatus(OrderStatus.CREATED)
                .date(new Date())
                .productList(orderProducts)
                .timeStart(new Time(new Date().getTime()))
                .totalPrice(getTotalPrice(orderProducts))
                .courier(courier)
                .restaurantAddress(restaurantAddress)
                .build();

        // todo добавить запись в список заказов курьера и ресторана
        Storage.courierActiveOrder.put(courier.getId(), order);

        return orderRepositories.save(order);
    }

    public Order setStatus(long id, OrderStatus status){

        if (status == OrderStatus.DELIVERED) {
            return completeOrder(id);
        }

        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        Order order = orderOptional.get();
        order.setOrderStatus(status);
        return orderRepositories.save(order);
    }

    public Order completeOrder(long id) {
        Optional<Order> orderOptional = orderRepositories.findById(id);
        if (orderOptional.isEmpty()) {
            throw new EntityNotFoundException("order not found");
        }

        Order order = orderOptional.get();
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setTimeEnd(new Time(new Date().getTime()));

        Storage.courierActiveOrder.remove(order.getCourier(), order);
        // todo

        return orderRepositories.save(order);
    }


    private double getTotalPrice(Set<OrderProduct> orderProducts ) {

        double price = 0D;

        for (OrderProduct orderProduct : orderProducts) {
            price += orderProduct.getProduct().getPrice() * orderProduct.getCount();
        }

        return price;
    }

    private static class Storage {

        private final static Map<Long, Order> courierActiveOrder = new LinkedHashMap<>();
    }







}
