package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.awt.*;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "orders")
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @ApiModelProperty
    @JoinColumn
    @ManyToOne
    private Restaurant restaurant;

    @ApiModelProperty
    @JoinColumn
    @ManyToOne
    private User user;

    @Column
    @ApiModelProperty
    private Double totalPrice;

    @ApiModelProperty
    @ManyToMany
    @JoinTable(
            name = "Order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> productList;

    @Column
    @ApiModelProperty
    private Date date;

    @Column
    @ApiModelProperty
    private OrderStatus orderStatus;

    @JoinColumn
    @ApiModelProperty
    @ManyToOne
    private Courier courier;

    @Column
    @ApiModelProperty
    private Time timeStart;

    @Column
    @ApiModelProperty
    private Time timeEnd;

}
