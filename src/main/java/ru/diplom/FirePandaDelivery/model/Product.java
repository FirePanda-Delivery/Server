package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Data
@Table
public class Product {

    @Id
    @ApiModelProperty
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @ApiModelProperty
    private String name;

    @Column(nullable = false)
    @JsonIgnore
    @ApiModelProperty
    private String normalizedName;

    @ManyToOne(targetEntity = Categories.class)
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Categories category;

    @Column(length = 1000)
    @ApiModelProperty
    private String description;

    @Column(nullable = false)
    @ApiModelProperty
    private double price;

    @Column(nullable = false)
    @ApiModelProperty
    private int weight;

    @Column
    @ApiModelProperty
    private float popularity;

    @Column
    @ApiModelProperty
    private String img = "/defaultImage/productDefault.png";

    @Column
    @JsonIgnore
    private boolean isDeleted;

    public void setName(String name) {
        this.name = name;
        this.normalizedName = name.toUpperCase(Locale.ROOT);
    }
}
