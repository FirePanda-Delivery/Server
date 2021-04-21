package ru.diplom.FirePandaDelivery.dto.requestModel;

import lombok.Data;

import java.util.Set;

@Data
public class CreateOrder {

    private String address;

    private Set<OrderProductReq> products;

    private long restaurantId;

    private long userId;

    private String city;

}
