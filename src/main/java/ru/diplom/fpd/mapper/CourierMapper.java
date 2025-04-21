package ru.diplom.fpd.mapper;

import lombok.Data;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.diplom.fpd.dto.CourierDto;
import ru.diplom.fpd.dto.requestModel.CourierReq;
import ru.diplom.fpd.model.Courier;
import ru.diplom.fpd.repositories.CitiesRepositories;
import ru.diplom.fpd.service.CitiesServices;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CitiesMapper.class, CitiesRepositories.class})
public abstract class CourierMapper {

    protected CitiesServices citiesServices;

    public abstract Courier toEntity(CourierDto courierDto);

    public abstract CourierDto toDto(Courier courier);

    @Mapping(target = "city", expression = "java(citiesServices.getByName(courierReq.getCity()))")
    public abstract Courier requestToEntity(CourierReq courierReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Courier partialUpdate(CourierDto courierDto, @MappingTarget Courier courier);
}