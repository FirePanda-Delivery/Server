package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    @ApiModelProperty
    private String name;

    @Column
    @ApiModelProperty
    private String description;

    @Column(nullable = false)
    @ApiModelProperty
    private Time workingHoursStart;

    @Column(nullable = false)
    @ApiModelProperty
    private Time workingHoursEnd;

    @Column
    @ApiModelProperty
    private double minPrice;

    @Column
    @ApiModelProperty
    private float rating;

    @Column
    @ApiModelProperty
    private boolean ownDelivery;

    @Column
    @ApiModelProperty
    @OneToMany
    @JoinColumn(name = "restaurant_id")
    private List<Categories> categories;

    @JoinColumn
    @ManyToMany
    @ApiModelProperty
    private List<Cities> Cities;

}
