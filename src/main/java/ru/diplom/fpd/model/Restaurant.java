package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Time;
import java.time.Duration;
import java.util.List;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "restaurant_id_sequence", allocationSize = 1)
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Time workingHoursStart;

    @Column(nullable = false)
    private Time workingHoursEnd;

    @Column
    private int minPrice;

    @Column

    private float rating;

    @Column
    private boolean ownDelivery;

    // fetch = FetchType.EAGER коряво работает
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    @SQLRestriction("is_deleted = FALSE")
    private List<Categories> categories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<AverageDeliveryTime> citiesDeliveryTimes;

    @OneToMany(mappedBy = "restaurant", cascade = {CascadeType.ALL})
    private List<RestaurantAddress> citiesAddress;

    @Column
    @JsonIgnore
    private boolean isDeleted;

    @Column

    private String img = "/defaultImage/restaurantDefault.png";

    @Column
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean published = false;
}
