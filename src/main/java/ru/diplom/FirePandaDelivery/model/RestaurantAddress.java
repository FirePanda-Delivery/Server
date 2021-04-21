package ru.diplom.FirePandaDelivery.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RestaurantAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String address;

    @ManyToOne
    private Cities city;

}
