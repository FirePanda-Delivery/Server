package ru.diplom.fpd.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.diplom.fpd.dto.RestaurantAddressDto;
import ru.diplom.fpd.dto.RestaurantDto;
import ru.diplom.fpd.dto.requestModel.CreateRestaurantDto;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;
import ru.diplom.fpd.repositories.CitiesRepositories;
import ru.diplom.fpd.repositories.RestaurantAddressRepositories;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class RestaurantMapper {

    @Autowired
    protected CitiesRepositories citiesRepositories;

    public abstract Restaurant toEntity(RestaurantDto restaurantDto);

    public abstract RestaurantDto toDto(Restaurant restaurant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Restaurant partialUpdate(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);

    @Mapping(target = "citiesAddress", ignore = true)
    public abstract Restaurant creteDtoToEntity(CreateRestaurantDto restaurantDto);

    @Mapping(target = "citiesAddress", ignore = true)
    public abstract Restaurant updateEntity(@MappingTarget Restaurant target, CreateRestaurantDto restaurantDto);

    @Mapping(target = "city", expression = "java(citiesRepositories" +
            ".findByCityIgnoreCase(restaurantAddressDto.getCity())" +
            ".orElseThrow(jakarta.persistence.EntityNotFoundException::new))")
    public abstract RestaurantAddress addressToEntity(RestaurantAddressDto restaurantAddressDto);

    @Mapping(target = "city", source = "city.city")
    public abstract RestaurantAddressDto toDto(RestaurantAddress restaurantAddress);
}