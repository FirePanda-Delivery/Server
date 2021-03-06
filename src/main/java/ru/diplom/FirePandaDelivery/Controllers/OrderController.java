package ru.diplom.FirePandaDelivery.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.service.CourierService;
import ru.diplom.FirePandaDelivery.service.OrderServices;
import ru.diplom.FirePandaDelivery.service.RestaurantService;
import ru.diplom.FirePandaDelivery.dto.requestModel.CreateOrder;
import ru.diplom.FirePandaDelivery.dto.requestModel.OrderProductReq;
import ru.diplom.FirePandaDelivery.exception.AddressNotInDeliveryAreaException;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.model.OrderStatus;
import ru.diplom.FirePandaDelivery.processing.AddressProcessing;

import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderServices orderServices;
    private final AddressProcessing validateAddress;
    private final RestaurantService restaurantService;
    private final CourierService courierService;

    @Autowired
    public OrderController(OrderServices orderServices, AddressProcessing validateAddress, RestaurantService restaurantService, CourierService courierService) {
        this.orderServices = orderServices;
        this.validateAddress = validateAddress;
        this.restaurantService = restaurantService;
        this.courierService = courierService;
    }

    @GetMapping("/CostDelivery")
    public ResponseEntity<Map<String, Double>> getCostDelivery() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("cost", 200D);
        return ResponseEntity.ok(map);
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

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrder createOrder) throws AddressNotInDeliveryAreaException {


        if (!validateAddress.isValid(createOrder.getAddress(), createOrder.getCity())) {
            throw new AddressNotInDeliveryAreaException();
        }

        Set<ru.diplom.FirePandaDelivery.model.OrderProduct> orderProducts = new HashSet<>();

        for (OrderProductReq product : createOrder.getProducts()) {
            ru.diplom.FirePandaDelivery.model.OrderProduct orderProduct = new ru.diplom.FirePandaDelivery.model.OrderProduct();
            orderProduct.setCount(product.getCount());
            orderProduct.setProduct(restaurantService.getProduct(product.getProductId()));

            orderProducts.add(orderProduct);
        }

        return ResponseEntity.ok(
                orderServices.createOrder(
                        createOrder.getUserId(),
                        createOrder.getRestaurantId(),
                        orderProducts,
                        createOrder.getAddress(),
                        createOrder.getCity()
                )
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable long id, @RequestBody OrderStatus status) {
        return ResponseEntity.ok(orderServices.setStatus(id, status));
    }


    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    /////////////////////// Exception handling /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////


    @ExceptionHandler({AddressNotInDeliveryAreaException.class})
    public ResponseEntity<String> AddressException() {
        return ResponseEntity.ok("the address is not in the delivery area");
    }



}
