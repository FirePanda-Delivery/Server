package ru.diplom.fpd.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.fpd.dto.ActiveCourier;
import ru.diplom.fpd.dto.Coordinates;
import ru.diplom.fpd.dto.requestModel.CourierReq;
import ru.diplom.fpd.service.CitiesServices;
import ru.diplom.fpd.service.CourierService;
import ru.diplom.fpd.service.OrderServices;
import ru.diplom.fpd.model.Courier;

import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {

    private final CourierService courierServices;
    private final OrderServices orderServices;
    private final CitiesServices citiesServices;


    public CourierController(CourierService courierServices, OrderServices orderServices, CitiesServices citiesServices) {
        this.courierServices = courierServices;
        this.orderServices = orderServices;
        this.citiesServices = citiesServices;
    }

    @GetMapping
    public ResponseEntity<List<Courier>> getCouriers() {
        return ResponseEntity.ok(courierServices.getCourierList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Courier> getCourier(@PathVariable long id) {
        return ResponseEntity.ok(courierServices.get(id));
    }

    @GetMapping("/{id}/order/active")
    public ResponseEntity<Object> getActiveOrder(@PathVariable long id) {
        return ResponseEntity.ok(orderServices.getActiveCourierOrder(id));
    }

    @PostMapping()
    public ResponseEntity<Courier> addCourier(@RequestBody CourierReq courierReq) {
        Courier courier = new Courier();
        courier.setPhone(courierReq .getPhone());
        courier.setLastName(courierReq.getLastName());
        courier.setFirstName(courierReq.getFirstName());
        courier.setEmail(courierReq.getEmail());
        courier.setCity(citiesServices.getByName(courierReq.getCity()));

        return ResponseEntity.ok(courierServices.add(courier));
    }

    @PostMapping(value = "/{id}/active")
    public ResponseEntity<Object> setActive(@PathVariable long id, @RequestBody Coordinates location) {
        CourierService.Storage.addCourier(courierServices.get(id), location);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Object> setLocation(@PathVariable long id, @RequestBody Coordinates location) {
        courierServices.setCourierLocation(courierServices.get(id), location);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/inactive")
    public ResponseEntity<Object> setInactive(@PathVariable long id, boolean isActive) {
        ActiveCourier activeCourier = CourierService.Storage.getActiveCourier(courierServices.get(id));

        if (activeCourier == null) {
            throw new NullPointerException();
        }

        CourierService.Storage.removeCourier(activeCourier);
        return ResponseEntity.ok().build();
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
