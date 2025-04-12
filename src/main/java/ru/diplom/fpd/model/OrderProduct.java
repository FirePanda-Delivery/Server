package ru.diplom.fpd.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int count;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Product product;
}
