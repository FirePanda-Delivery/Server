package ru.diplom.FirePandaDelivery.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    private final List<OrderStatus> FINAL_STATUSES = Arrays.asList(OrderStatus.DELIVERED, OrderStatus.CANCELED);



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

    public List<Order> getActiveRestaurantOrder(long restaurantId) {

        // todo додумать как сделать выгрузку из базы в локальное хранилище

        List<Order> orderList = Storage.restaurantActiveOrder.get(restaurantId);

        if (orderList == null || orderList.isEmpty()) {

            List<Order> orders = orderRepositories.findAllByRestaurant_IdAndOrderStatusIsNotIn(restaurantId, FINAL_STATUSES);

            Storage.addRestaurantActiveOrderList(restaurantId, orders);

            return orderList;
        }

        return orderList;
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


        Order order = new Order()
                .setAddress(address)
                .setUser(userService.get(userId))
                .setRestaurant(restaurant)
                .setOrderStatus(OrderStatus.CREATED)
                .setDate(new Date())
                .setProductList(orderProducts)
                .setTimeStart(new Time(new Date().getTime()))
                .setTotalPrice(getTotalPrice(orderProducts))
                .setCourier(courier)
                .setRestaurantAddress(restaurantAddress);

        Storage.courierActiveOrder.put(courier.getId(), order);
        Storage.addRestaurantActiveOrder(restaurantId, order);

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

        Storage.courierActiveOrder.remove(order.getCourier().getId(), order);
        Storage.restaurantActiveOrder.remove(order.getRestaurant().getId(), order);

        return orderRepositories.save(order);
    }


    private double getTotalPrice(Set<OrderProduct> orderProducts ) {

        double price = 0D;

        for (OrderProduct orderProduct : orderProducts) {
            price += orderProduct.getProduct().getPrice() * orderProduct.getCount();
        }

        return price;
    }

    public static class Storage {

        /**
         * stores data about active orders and assigned couriers
         * param Long - courier id
         * param Order - order
         */
        private final static Map<Long, Order> courierActiveOrder = new LinkedHashMap<>();


        private final static Map<Long, List<Order>> restaurantActiveOrder = new LinkedHashMap<>();

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
