package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Builder
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
    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<OrderProduct> productList;

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


    @Column
    @ApiModelProperty
    private String address;

}
