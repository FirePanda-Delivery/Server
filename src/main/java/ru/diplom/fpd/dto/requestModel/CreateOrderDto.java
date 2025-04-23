package ru.diplom.fpd.dto.requestModel;

import lombok.Data;

import java.util.Set;
import ru.diplom.fpd.dto.OrderProductDto;

@Data
public class CreateOrderDto {

    private String address;

    private Set<OrderProductDto> products;

    private long restaurantId;

    private long userId;

    private String city;

}
