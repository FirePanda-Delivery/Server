package ru.diplom.FirePandaDelivery.model;

import lombok.Data;

import javax.persistence.*;

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
