package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class Product {

    @Id
    @ApiModelProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    @ApiModelProperty
    private String name;

    @Column
    @ApiModelProperty
    private String description;

    @Column(nullable = false)
    @ApiModelProperty
    private double price;

    @Column
    @ApiModelProperty
    private float popularity;

    @Column
    @ApiModelProperty
    private String img;



}
