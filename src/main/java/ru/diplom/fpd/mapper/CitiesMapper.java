package ru.diplom.fpd.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.dto.CitiesDto;
import ru.diplom.fpd.model.Cities;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CitiesMapper {
    Cities toEntity(CitiesDto citiesDto);

    CitiesDto toDto(Cities cities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Cities partialUpdate(CitiesDto citiesDto, @MappingTarget Cities cities);
}