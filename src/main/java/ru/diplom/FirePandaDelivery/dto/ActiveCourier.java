package ru.diplom.FirePandaDelivery.dto;

import lombok.Builder;
import lombok.Data;
import ru.diplom.FirePandaDelivery.model.Courier;

@Data
public class ActiveCourier {

    private Courier courier;

    private boolean onOrder;

    private Coordinates location;

}
