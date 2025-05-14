package ru.diplom.fpd.mapper;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import ru.diplom.fpd.dto.AverageDeliveryTimeDto;
import ru.diplom.fpd.dto.RestaurantAddressDto;
import ru.diplom.fpd.dto.RestaurantDto;
import ru.diplom.fpd.dto.requestModel.CreateRestaurantDto;
import ru.diplom.fpd.model.AverageDeliveryTime;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;
import ru.diplom.fpd.repositories.CitiesRepositories;
import ru.diplom.fpd.repositories.RestaurantAddressRepositories;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AverageDeliveryTimeMapper.class, CitiesStringMapper.class})
public interface RestaurantMapper {

    Restaurant toEntity(RestaurantDto restaurantDto);

    @Mapping(target = "addresses", source = "citiesAddress")
    @Mapping(target = "averageDeliveryTimes", source = "citiesDeliveryTimes")
    @Mapping(target = "allCitiesAvgDeliveryTime", source = "citiesDeliveryTimes",
            qualifiedByName = "getAllCitiesAvgDeliveryTime")
    RestaurantDto toDto(Restaurant restaurant);

    @Named("getAllCitiesAvgDeliveryTime")
    default short getAllCitiesAvgDeliveryTime(List<AverageDeliveryTime> averageDeliveryTimes) {
        return (short) averageDeliveryTimes.stream()
                .map(AverageDeliveryTime::getAvgDeliveryTime)
                .mapToInt(Short::intValue)
                .average()
                .orElse(0);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Restaurant partialUpdate(RestaurantDto restaurantDto, @MappingTarget Restaurant restaurant);

    @Mapping(target = "citiesAddress", ignore = true)
    Restaurant creteDtoToEntity(CreateRestaurantDto restaurantDto);

    @Mapping(target = "citiesAddress", ignore = true)
    Restaurant updateEntity(@MappingTarget Restaurant target, CreateRestaurantDto restaurantDto);

    RestaurantAddress addressToEntity(RestaurantAddressDto restaurantAddressDto);

    @Mapping(target = "city", source = "city.city")
    RestaurantAddressDto toDto(RestaurantAddress restaurantAddress);
}