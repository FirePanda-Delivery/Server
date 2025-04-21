package ru.diplom.fpd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import jakarta.persistence.*;
import java.util.List;
import java.util.Locale;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "city_id_sequence", allocationSize = 1)
    private long id;

    @JsonValue
    @Column
    private String city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Column(nullable = false, unique = true)
    @JoinColumn(name = "city_id")
    private List<CitiesCoordinates> coordinates;
}
