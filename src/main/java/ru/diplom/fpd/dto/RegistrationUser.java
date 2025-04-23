package ru.diplom.fpd.dto;

import lombok.Data;

@Data
public class RegistrationUser {

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;
}
