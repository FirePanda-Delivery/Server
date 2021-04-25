package ru.diplom.FirePandaDelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ApiModelProperty
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties({
            "description", "workingHoursStart", "workingHoursEnd",
            "minPrice", "ownDelivery", "categories",
            "citiesAddress", "img"
    })
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

    @JoinColumn(nullable = true)
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

    @ManyToOne
    @JoinColumn(nullable = false)
    @ApiModelProperty
    private Cities cities;


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

    public Order setCities(Cities cities) {
        this.cities = cities;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (getId() != order.getId()) return false;
        if (getRestaurant() != null ? !getRestaurant().equals(order.getRestaurant()) : order.getRestaurant() != null)
            return false;
        if (getUser() != null ? !getUser().equals(order.getUser()) : order.getUser() != null) return false;
        if (getTotalPrice() != null ? !getTotalPrice().equals(order.getTotalPrice()) : order.getTotalPrice() != null)
            return false;
        if (getProductList() != null ? !getProductList().equals(order.getProductList()) : order.getProductList() != null)
            return false;
        if (getDate() != null ? !getDate().equals(order.getDate()) : order.getDate() != null) return false;
        if (getOrderStatus() != order.getOrderStatus()) return false;
        if (getCourier() != null ? !getCourier().equals(order.getCourier()) : order.getCourier() != null) return false;
        if (getTimeStart() != null ? !getTimeStart().equals(order.getTimeStart()) : order.getTimeStart() != null)
            return false;
        if (getTimeEnd() != null ? !getTimeEnd().equals(order.getTimeEnd()) : order.getTimeEnd() != null) return false;
        if (getAddress() != null ? !getAddress().equals(order.getAddress()) : order.getAddress() != null) return false;
        return getRestaurantAddress() != null ? getRestaurantAddress().equals(order.getRestaurantAddress()) : order.getRestaurantAddress() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getRestaurant() != null ? getRestaurant().hashCode() : 0);
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        result = 31 * result + (getTotalPrice() != null ? getTotalPrice().hashCode() : 0);
        result = 31 * result + (getProductList() != null ? getProductList().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getOrderStatus() != null ? getOrderStatus().hashCode() : 0);
        result = 31 * result + (getCourier() != null ? getCourier().hashCode() : 0);
        result = 31 * result + (getTimeStart() != null ? getTimeStart().hashCode() : 0);
        result = 31 * result + (getTimeEnd() != null ? getTimeEnd().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getRestaurantAddress() != null ? getRestaurantAddress().hashCode() : 0);
        return result;
    }
}


