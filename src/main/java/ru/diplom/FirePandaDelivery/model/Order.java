package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


//    @ApiModelProperty
//    @ManyToOne
//    private Restaurant restaurant;

}
