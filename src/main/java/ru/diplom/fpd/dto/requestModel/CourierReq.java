package ru.diplom.fpd.dto.requestModel;

import lombok.Data;

@Data
public class CourierReq {

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String city;

}
