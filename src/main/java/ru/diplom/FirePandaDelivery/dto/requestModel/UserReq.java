package ru.diplom.FirePandaDelivery.dto.requestModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class UserReq {

    private String userName;

    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;
}
