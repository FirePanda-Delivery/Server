package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.LinkedList;
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

    @ApiModelProperty
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private List<Categories> categories;

    @JoinColumn
    @ManyToMany
    @ApiModelProperty
    private List<Cities> Cities;

    @Column
    @JsonIgnore
    private boolean isDeleted;

    @Column
    private String img;

}
