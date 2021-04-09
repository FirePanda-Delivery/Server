package ru.diplom.FirePandaDelivery.dto.requestModel;

import lombok.Data;

@Data
public class OrderProductReq {

    private long productId;

    private int count;

}
