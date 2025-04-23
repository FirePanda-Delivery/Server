package ru.diplom.fpd.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.diplom.fpd.dto.CourierDto;
import ru.diplom.fpd.dto.requestModel.CourierReq;
import ru.diplom.fpd.model.Courier;
import ru.diplom.fpd.repositories.CitiesRepositories;
import ru.diplom.fpd.service.CitiesServices;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CitiesMapper.class, CitiesRepositories.class})
@RequiredArgsConstructor()
public abstract class CourierMapper {

    @Autowired
    protected CitiesServices citiesServices;

    public abstract Courier toEntity(CourierDto courierDto);

    public abstract CourierDto toDto(Courier courier);

    @Mapping(target = "city", expression = "java(citiesServices.getByName(courierReq.getCity()))")
    public abstract Courier requestToEntity(CourierReq courierReq);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Courier partialUpdate(CourierDto courierDto, @MappingTarget Courier courier);
}