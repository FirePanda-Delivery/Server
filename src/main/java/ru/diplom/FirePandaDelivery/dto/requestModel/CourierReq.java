package ru.diplom.FirePandaDelivery.dto.requestModel;

import lombok.Data;
import ru.diplom.FirePandaDelivery.model.Cities;

@Data
public class CourierReq {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String city;

}
