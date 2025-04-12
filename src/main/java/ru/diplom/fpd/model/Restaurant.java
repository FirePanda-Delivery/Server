package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Time;
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
    private String name;

    @Column(unique = true, nullable = false)
    @JsonIgnore
    private String normalizedName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Time workingHoursStart;

    @Column(nullable = false)
    private Time workingHoursEnd;

    @Column
    private double minPrice;

    @Column
    
    private float rating;

    @Column
    private boolean ownDelivery;

    // fetch = FetchType.EAGER коряво работает
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private List<Categories> categories;

    @JoinColumn(name = "restaurant_id")
    @OneToMany(cascade = CascadeType.ALL)
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
