package ru.diplom.fpd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

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
    private int price;

    @Column(nullable = false)
    private int weight;

    @Column
    private float popularity;

    @Column
    private String img = "/defaultImage/productDefault.png";

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
