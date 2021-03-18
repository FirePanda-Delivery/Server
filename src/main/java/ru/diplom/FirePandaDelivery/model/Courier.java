package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ApiModelProperty
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @ApiModelProperty
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @ApiModelProperty
    @Column(name = "PHONE", nullable = false, unique = true)
    private String phone;

    @ApiModelProperty
    @Column(name = "EMAIL")
    private String email;

    @Column
    @ApiModelProperty
    private float rating;

    @Column
    @ApiModelProperty
    private String city;

}
