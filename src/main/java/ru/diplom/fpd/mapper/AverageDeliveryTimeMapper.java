package ru.diplom.fpd.mapper;

import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.dto.AverageDeliveryTimeDto;
import ru.diplom.fpd.dto.DeliveryTimeDto;
import ru.diplom.fpd.model.AverageDeliveryTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CitiesStringMapper.class})
public interface AverageDeliveryTimeMapper {

    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "avgDeliveryTime", source = "deliveryTime")
    AverageDeliveryTime toEntity(AverageDeliveryTimeDto averageDeliveryTimeDto);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "avgDeliveryTime", target = "deliveryTime")
    @Mapping(source = "city.city", target = "city")
    AverageDeliveryTimeDto toDto(AverageDeliveryTime averageDeliveryTime);

    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "avgDeliveryTime", source = "deliveryTime")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AverageDeliveryTime partialUpdate(AverageDeliveryTimeDto averageDeliveryTimeDto, @MappingTarget AverageDeliveryTime averageDeliveryTime);
}