package ru.diplom.fpd.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.fpd.dto.CitiesCoordinatesDto;
import ru.diplom.fpd.service.CitiesServices;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/city")
public class CityController {

    private final CitiesServices cityServices;

    @GetMapping("/coordinates")
    public ResponseEntity<List<CitiesCoordinatesDto>> get(@RequestParam(name = "city") String city) {



        return ResponseEntity.ok(cityServices.getCoordinatesByName(city));
    }

}
