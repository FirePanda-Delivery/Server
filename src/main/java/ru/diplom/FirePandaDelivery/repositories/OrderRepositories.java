package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.model.OrderStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepositories extends JpaRepository<Order, Long> {

    List<Order> findAllByUser_Id(long id);

    List<Order> findAllByCourierId(long id);

    List<Order> findAllByRestaurant_Id(long id);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    Optional<Order> findByCourier_IdAndOrderStatusIsNotIn(long courier_id, Iterable<OrderStatus> orderStatus);

    List<Order> findAllByRestaurant_IdAndOrderStatusIsNotIn(long restaurant_id, Iterable<OrderStatus> orderStatus);
}
