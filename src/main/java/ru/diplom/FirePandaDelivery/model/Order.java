package ru.diplom.FirePandaDelivery.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Table(name = "orders")
public class Order {


    public Order() {

    }

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

    public Order setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }

    public Order setUser(User user) {
        this.user = user;
        return this;
    }

    public Order setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public Order setProductList(Set<OrderProduct> productList) {
        this.productList = productList;
        return this;
    }

    public Order setDate(Date date) {
        this.date = date;
        return this;
    }

    public Order setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public Order setCourier(Courier courier) {
        this.courier = courier;
        return this;
    }

    public Order setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public Order setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    public Order setAddress(String address) {
        this.address = address;
        return this;
    }

    public Order setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
        return this;
    }
}


