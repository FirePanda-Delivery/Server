package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@Data
@Entity
@Table
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty
    private long id;

    @Column(nullable = false)
    @ApiModelProperty
    private String name;

    @JsonIgnore
    @Column(nullable = false)
    @ApiModelProperty
    private String normalizedName;

    @ApiModelProperty
    @JoinColumn(name = "category_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> products;


    @ManyToOne(targetEntity = Restaurant.class)
    @JsonIgnore
    @JoinColumn
    private Restaurant restaurant;

    @Column
    @JsonIgnore
    private boolean isDeleted;

    public void setName(String name) {
        this.name = name;
        this.normalizedName = name.toUpperCase(Locale.ROOT);
    }
}
