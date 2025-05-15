package ru.diplom.fpd.dto.requestModel;

import lombok.Data;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import ru.diplom.fpd.dto.RestaurantAddressDto;

@Data
public class CreateRestaurantDto {

    private long id;

    private String name;

    private String description;

    private Time workingHoursStart;

    private Time workingHoursEnd;

    private int minPrice;

    private boolean ownDelivery;

    private List<RestaurantAddressDto> citiesAddress;

}
