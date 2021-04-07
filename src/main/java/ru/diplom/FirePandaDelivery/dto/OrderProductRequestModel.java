package ru.diplom.FirePandaDelivery.dto;

import lombok.Data;

@Data
public class OrderProductRequestModel {

    private long productId;

    private int count;

}
