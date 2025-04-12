package ru.diplom.fpd.dto.requestModel;

import lombok.Data;

@Data
public class OrderProductReq {

    private long productId;

    private int count;

}
