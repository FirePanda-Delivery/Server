package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "coordinates")
@Data
public class CitiesCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty
    private long id;

    private double x;

    private double y;

}
