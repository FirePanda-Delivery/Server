package ru.diplom.fpd.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.diplom.fpd.dto.CitiesCoordinatesDto;
import ru.diplom.fpd.dto.CitiesDto;
import ru.diplom.fpd.model.CitiesCoordinates;
import ru.diplom.fpd.model.City;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CitiesMapper {
    City toEntity(CitiesDto citiesDto);

    CitiesDto toDto(City city);

    CitiesCoordinatesDto toCoordinatesDto(CitiesCoordinates coordinates);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    City partialUpdate(CitiesDto citiesDto, @MappingTarget City city);
}