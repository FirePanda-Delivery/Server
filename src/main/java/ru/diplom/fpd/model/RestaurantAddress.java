package ru.diplom.fpd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "restaurant_address_id_sequence", allocationSize = 1)
    private long id;

    private String address;

    @ManyToOne
    private Cities city;

}
