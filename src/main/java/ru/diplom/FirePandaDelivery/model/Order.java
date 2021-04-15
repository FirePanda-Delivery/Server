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
    @JoinColumn(nullable = false)
    @ManyToOne
    private Restaurant restaurant;

    @ApiModelProperty
    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    @Column(nullable = false)
    @ApiModelProperty()
    private Double totalPrice;

    @ApiModelProperty
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Set<OrderProduct> productList;

    @Column(nullable = false)
    @ApiModelProperty
    private Date date;

    @Column(nullable = false)
    @ApiModelProperty
    private OrderStatus orderStatus;

    @JoinColumn(nullable = false)
    @ApiModelProperty
    @ManyToOne
    private Courier courier;

    @Column(nullable = false)
    @ApiModelProperty
    private Time timeStart;

    @Column
    @ApiModelProperty
    private Time timeEnd;


    @Column(nullable = false)
    @ApiModelProperty
    private String address;

    @Column(nullable = false)
    @ApiModelProperty
    private String restaurantAddress;

}
