package ru.diplom.fpd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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


    @Operation(summary = "Получить список всех курьеров")
    @GetMapping
    public ResponseEntity<List<CourierDto>> getCouriers() {
        return ResponseEntity.ok(courierServices.getCourierList());
    }

    @Operation(summary = "Получить данные курьера по id", parameters = {
            @Parameter(name = "id", description = "Идетификатор курьера", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getCourier(@PathVariable long id) {
        return ResponseEntity.ok(courierServices.get(id));
    }

    @Operation(summary = "Создать курьера")
    @PostMapping
    public ResponseEntity<CourierDto> addCourier(@RequestBody CourierReq courierReq) {
        return ResponseEntity.ok(courierServices.add(courierReq));
    }

    @Operation(summary = "Изменить статус курьера на активный", parameters = {
            @Parameter(name = "id", description = "Идетификатор курьера", in = ParameterIn.PATH, required = true)
    })
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

    @Operation(summary = "Изменить статус курьера на не активный", parameters = {
            @Parameter(name = "id", description = "Идетификатор курьера", in = ParameterIn.PATH, required = true)
    })
    @PostMapping(value = "/{id}/inactive")
    public ResponseEntity<Object> setInactive(@PathVariable long id) {
        courierServices.setInactive(id);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/addList")
//    public ResponseEntity<List<Courier>> addCouriers(@RequestBody List<Courier> couriers) {
//        return ResponseEntity.ok(courierServices.addCourierList(couriers));
//    }

    @Operation(summary = "Обновить данные курьера", description = "Если не указать поле оно встанет в null",
            parameters = {
                    @Parameter(name = "id", description = "Идетификатор курьера", in = ParameterIn.PATH, required = true)
            })
    @PutMapping
    public ResponseEntity<CourierDto> updateCourier(@RequestBody CourierDto courier) {
        return ResponseEntity.ok(courierServices.update(courier));
    }

    @Operation(summary = "Помечает курьера удаленным", parameters = {
            @Parameter(name = "id", description = "Идетификатор курьера", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/{id}")
    public void deleteCourier(@PathVariable long id) {
        courierServices.delete(id);
    }

}
