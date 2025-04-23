package ru.diplom.fpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.diplom.fpd.model.OrderStatus;

/**
 * DTO for {@link ru.diplom.fpd.model.Order}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto implements Serializable {
    private final long id;
    private final RestaurantDto restaurant;
    private final Double totalPrice;
    private final Set<OrderProductDto> productList;
    private final Date date;
    private final OrderStatus orderStatus;
    private final CourierDto courier;
    private final Time timeStart;
    private final Time timeEnd;
    private final String address;
    private final CitiesDto cities;
    private final RestaurantAddressDto restaurantAddress;
}