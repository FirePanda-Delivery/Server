package ru.diplom.fpd.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class RestaurantAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String address;

    @ManyToOne
    private Cities city;

}
