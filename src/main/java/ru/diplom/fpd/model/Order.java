package ru.diplom.fpd.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Time;
import java.util.Date;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDER_SEQUENCE")
    @SequenceGenerator(name = "ORDER_SEQUENCE", sequenceName = "order_id_sequence", allocationSize = 1)
    private long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Restaurant restaurant;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", nullable = false)
    private Set<OrderProduct> productList;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JoinColumn(nullable = true)
    @ManyToOne
    private Courier courier;

    @Column(nullable = false)
    private Time timeStart;

    @Column
    private Time timeEnd;


    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(nullable = false, name = "city_id")
    private City city;


    @ManyToOne
    @JoinColumn(nullable = false, name = "restaurant_address_id")
    private RestaurantAddress restaurantAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (getId() != order.getId()) return false;
        if (getRestaurant() != null ? !getRestaurant().equals(order.getRestaurant()) : order.getRestaurant() != null)
            return false;
        if (getUser() != null ? !getUser().equals(order.getUser()) : order.getUser() != null) return false;
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


