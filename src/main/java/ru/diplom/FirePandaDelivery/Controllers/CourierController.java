package ru.diplom.FirePandaDelivery.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.Service.CourierService;
import ru.diplom.FirePandaDelivery.model.Courier;

import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {

    private CourierService courierServices;


    public CourierController(CourierService courierServices) {
        this.courierServices = courierServices;
    }

    @GetMapping
    public ResponseEntity<List<Courier>> getCouriers() {
        return ResponseEntity.ok(courierServices.getCourierList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courier> getCourier(@PathVariable long id) {
        return ResponseEntity.ok(courierServices.get(id));
    }

    @GetMapping("/order/active")
    public ResponseEntity<Object> getActiveOrder() {
        return ResponseEntity.ok("тут будет активный заказ данного курьера");
    }

    @PostMapping()
    public ResponseEntity<Courier> addCourier(@RequestBody Courier courier) {
        return ResponseEntity.ok(courierServices.add(courier));
    }

    @PostMapping("/addList")
    public ResponseEntity<List<Courier>> addCouriers(@RequestBody List<Courier> couriers) {
        return ResponseEntity.ok(courierServices.addCourierList(couriers));
    }

    @PutMapping
    public ResponseEntity<Courier> updateCourier(@RequestBody Courier courier) {
        return ResponseEntity.ok(courierServices.update(courier));
    }

    @DeleteMapping("/{id}")
    public void deleteCourier(@PathVariable long id) {
        courierServices.delete(id);
    }

}
