package ru.diplom.fpd.dto.requestModel;


import lombok.Data;

@Data
public class UserReq {

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;
}
