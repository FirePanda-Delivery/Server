package ru.diplom.fpd.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.diplom.fpd.dto.CitiesCoordinatesDto;
import ru.diplom.fpd.dto.CitiesDto;
import ru.diplom.fpd.model.CitiesCoordinates;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.repositories.CitiesRepositories;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public abstract class CitiesStringMapper {

    @Autowired
    private CitiesRepositories citiesRepositories;

    City toEntity(String city) {
        return citiesRepositories.findByCityIgnoreCase(city)
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
    }

}