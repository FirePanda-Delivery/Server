package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Locale;

@Entity
@Data
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonIgnore
    private String normalizedName;

    @ManyToOne(targetEntity = Categories.class)
    @JsonIgnore
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
    @JsonIgnore
    private boolean isDeleted;

    public void setName(String name) {
        this.name = name;
        this.normalizedName = name.toUpperCase(Locale.ROOT);
    }
}
