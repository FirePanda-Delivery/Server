package ru.diplom.FirePandaDelivery.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.Service.OrderServices;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.model.OrderProduct;
import ru.diplom.FirePandaDelivery.model.OrderStatus;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order")
public class OrderController {

    final OrderServices orderServices;


    public OrderController(OrderServices orderServices) {
        this.orderServices = orderServices;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getOrder(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable long id) {
        return  ResponseEntity.ok(orderServices.getUserOrders(id));
    }

    @GetMapping("/courier/{id}")
    public ResponseEntity<List<Order>> getCourierOrders(@PathVariable long id) {
        return  ResponseEntity.ok(orderServices.getCourierOrders(id));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Order>> getRestaurantOrders(@PathVariable long id) {
        return  ResponseEntity.ok(orderServices.getRestaurantOrders(id));
    }

    @PostMapping(params = {"restId", "userId"})
    public ResponseEntity<Order> createOrder(
            @RequestBody Set<OrderProduct> orderProducts,
            long restId,
            long userId,
            @CookieValue("address") String address
    ) {

        // TODO добавить проверку вхождения адреса в зону доставки

        return ResponseEntity.ok(
                orderServices.createOrder(userId, restId, orderProducts, address)
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable long id, @RequestBody OrderStatus status) {
        return ResponseEntity.ok(orderServices.setStatus(id, status));
    }



}
