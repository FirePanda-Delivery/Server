package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.persistence.*;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Entity
@Table
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    @ApiModelProperty
    private String name;

    @Column(unique = true, nullable = false)
    @JsonIgnore
    @ApiModelProperty
    private String normalizedName;

    @Column(length = 1000)
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

    // fetch = FetchType.EAGER коряво работает
    @ApiModelProperty
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private List<Categories> categories;

    @JoinColumn(name = "restaurant_id")
    @OneToMany(cascade = CascadeType.ALL)
    @ApiModelProperty
    private List<RestaurantAddress> citiesAddress;

    @Column
    @JsonIgnore
    private boolean isDeleted;

    @Column
    private String img = "/defaultImage/restaurantDefault.png";

    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean published = false;

    public void setName(String name) {
        this.name = name;
        this.normalizedName = name.toUpperCase(Locale.ROOT);
    }
}
