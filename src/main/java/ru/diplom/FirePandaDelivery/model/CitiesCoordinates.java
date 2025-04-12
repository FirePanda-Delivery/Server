package ru.diplom.FirePandaDelivery.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "coordinates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitiesCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double x;

    private double y;

    private int index;

}
