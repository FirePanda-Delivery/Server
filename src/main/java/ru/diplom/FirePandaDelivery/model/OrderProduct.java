package ru.diplom.FirePandaDelivery.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private int count;

    @JoinColumn
    @ManyToOne
    private Product product;
}
