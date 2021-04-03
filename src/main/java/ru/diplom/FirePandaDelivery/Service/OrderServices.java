package ru.diplom.FirePandaDelivery.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.model.OrderProduct;
import ru.diplom.FirePandaDelivery.model.OrderStatus;
import ru.diplom.FirePandaDelivery.repositories.OrderRepositories;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderServices {

    private final OrderRepositories orderRepositories;

    private final UserService userService;
    private final CourierService courierService;
    private final RestaurantService restaurantService;

    @Autowired
    public OrderServices(OrderRepositories orderRepositories, UserService userService, CourierService courierService, RestaurantService restaurantService) {
        this.orderRepositories = orderRepositories;
        this.userService = userService;
        this.courierService = courierService;
        this.restaurantService = restaurantService;
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


    public Order createOrder(long userId, long restaurantId, Set<OrderProduct> orderProducts, String address ) {

        if (orderProducts.isEmpty()) {
            throw new NullPointerException("products not set");
        }

        if (address == null || address.isEmpty() || address.equals(" ")) {
            throw new NullPointerException("address not set");
        }


        Order order = Order.builder()
                .address(address)
                .user(userService.get(userId))
                .restaurant(restaurantService.getRestaurant(restaurantId))
                .orderStatus(OrderStatus.CREATED)
                .date(new Date())
                .productList(orderProducts)
                .timeStart(new Time(new Date().getTime()))
                .totalPrice(getTotalPrice(orderProducts))
               //.courier(null) в скобках бутед вызов к логики рапределения TODO: добавить курьера
                .build();

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

        return orderRepositories.save(order);
    }


    private double getTotalPrice(Set<OrderProduct> orderProducts ) {

        double price = 0D;

        for (OrderProduct orderProduct : orderProducts) {
            price += orderProduct.getProduct().getPrice() * orderProduct.getCount();
        }

        return price;
    }







}