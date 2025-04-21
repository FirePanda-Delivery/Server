package ru.diplom.fpd.controllers;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.fpd.dto.Coordinates;
import ru.diplom.fpd.dto.CourierDto;
import ru.diplom.fpd.dto.requestModel.CourierReq;
import ru.diplom.fpd.service.CitiesServices;
import ru.diplom.fpd.service.CourierService;
import ru.diplom.fpd.service.OrderServices;

@RestController
@AllArgsConstructor
@RequestMapping("/courier")
public class CourierController {

    private final CourierService courierServices;
    private final OrderServices orderServices;
    private final CitiesServices citiesServices;


    @GetMapping
    public ResponseEntity<List<CourierDto>> getCouriers() {
        return ResponseEntity.ok(courierServices.getCourierList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getCourier(@PathVariable long id) {
        return ResponseEntity.ok(courierServices.get(id));
    }

    @PostMapping
    public ResponseEntity<CourierDto> addCourier(@RequestBody CourierReq courierReq) {
        return ResponseEntity.ok(courierServices.add(courierReq));
    }

    @PostMapping(value = "/{id}/active")
    public ResponseEntity<Object> setActive(@PathVariable long id, @RequestBody Coordinates location) {
        courierServices.setActive(id, location);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<Object> setLocation(@PathVariable long id, @RequestBody Coordinates location) {
        courierServices.setCourierLocation(id, location);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{id}/inactive")
    public ResponseEntity<Object> setInactive(@PathVariable long id) {
        courierServices.setInactive(id);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/addList")
//    public ResponseEntity<List<Courier>> addCouriers(@RequestBody List<Courier> couriers) {
//        return ResponseEntity.ok(courierServices.addCourierList(couriers));
//    }

    @PutMapping
    public ResponseEntity<CourierDto> updateCourier(@RequestBody CourierDto courier) {
        return ResponseEntity.ok(courierServices.update(courier));
    }

    @DeleteMapping("/{id}")
    public void deleteCourier(@PathVariable long id) {
        courierServices.delete(id);
    }

}
