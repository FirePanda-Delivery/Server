package ru.diplom.fpd.dto;

import lombok.Data;
import ru.diplom.fpd.model.Courier;

@Data
public class ActiveCourier {

    private Courier courier;

    private boolean onOrder;

    private Coordinates location;

}
