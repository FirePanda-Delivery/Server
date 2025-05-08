package ru.diplom.fpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link ru.diplom.fpd.model.Restaurant}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDto implements Serializable {
    private final long id;
    private final String name;
    private final String description;
    private final Time workingHoursStart;
    private final Time workingHoursEnd;
    private final double minPrice;
    private final float rating;
    private final boolean ownDelivery;
    private final List<RestaurantAddressDto> addresses;
    private final String img;
}