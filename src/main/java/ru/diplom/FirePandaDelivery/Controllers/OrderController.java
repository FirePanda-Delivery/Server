package ru.diplom.FirePandaDelivery.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.Service.OrderServices;
import ru.diplom.FirePandaDelivery.Service.RestaurantService;
import ru.diplom.FirePandaDelivery.dto.requestModel.OrderProductReq;
import ru.diplom.FirePandaDelivery.exception.AddressNotInDeliveryAreaException;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.model.OrderStatus;
import ru.diplom.FirePandaDelivery.validate.ValidateAddress;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderServices orderServices;
    private final ValidateAddress validateAddress;
    private final RestaurantService restaurantService;

    @Autowired
    public OrderController(OrderServices orderServices, ValidateAddress validateAddress, RestaurantService restaurantService) {
        this.orderServices = orderServices;
        this.validateAddress = validateAddress;
        this.restaurantService = restaurantService;
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
            @RequestBody Set<OrderProductReq> products,
            long restId,
            long userId,
            @CookieValue("address") String address) {

        //TODO разобраться с куками

        if (!validateAddress.isValid(address, address.split(",")[1].trim())) {
            throw new AddressNotInDeliveryAreaException();
        }

        Set<ru.diplom.FirePandaDelivery.model.OrderProduct> orderProducts = new HashSet<>();

        for (OrderProductReq product : products) {
            ru.diplom.FirePandaDelivery.model.OrderProduct orderProduct = new ru.diplom.FirePandaDelivery.model.OrderProduct();
            orderProduct.setCount(product.getCount());
            orderProduct.setProduct(restaurantService.getProduct(product.getProductId()));

            orderProducts.add(orderProduct);
        }

        return ResponseEntity.ok(
                orderServices.createOrder(userId, restId, orderProducts, address)
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
