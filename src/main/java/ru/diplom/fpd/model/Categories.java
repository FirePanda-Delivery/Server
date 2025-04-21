package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import jakarta.persistence.*;
import java.util.List;
import java.util.Locale;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Data
@Entity
@Table
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "category_id_sequence", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private String name;

    @JoinColumn(name = "category_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @SQLRestriction("isDeleted = FALSE")
    private List<Product> products;


    @ManyToOne(targetEntity = Restaurant.class)
    @JsonIgnore
    @JoinColumn
    private Restaurant restaurant;

    @Column
    @JsonIgnore
    private boolean isDeleted;

}
