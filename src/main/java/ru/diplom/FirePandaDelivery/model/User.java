package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

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

}
