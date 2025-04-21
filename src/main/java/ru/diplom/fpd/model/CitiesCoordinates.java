package ru.diplom.fpd.model;

import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "coordinates_id_sequence", allocationSize = 1)
    private long id;

    private double x;

    private double y;

    private int index;

}
