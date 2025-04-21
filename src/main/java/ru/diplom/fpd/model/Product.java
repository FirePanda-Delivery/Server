package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Locale;

@Entity
@Data
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "product_id_sequence", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = Categories.class)
    @JoinColumn(name = "category_id")
    private Categories category;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int weight;

    @Column
    private float popularity;

    @Column
    private String img = "/defaultImage/productDefault.png";

    @Column
    private boolean isDeleted;
}
