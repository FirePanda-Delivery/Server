package ru.diplom.fpd.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "order_product_id_sequence", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private int count;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Product product;
}
