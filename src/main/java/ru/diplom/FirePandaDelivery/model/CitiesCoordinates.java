package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "coordinates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitiesCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty
    private long id;

    private double x;

    private double y;

}
