package ru.diplom.fpd.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.Order;
import ru.diplom.fpd.model.OrderStatus;

public interface OrderRepositories extends JpaRepository<Order, Long> {

    List<Order> findAllByUser_Id(long id);

    List<Order> findAllByCourierId(long id);

    List<Order> findAllByRestaurant_Id(long id);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    Optional<Order> findByCourier_IdAndOrderStatusIsNotIn(long courier_id, Iterable<OrderStatus> orderStatus);

    List<Order> findAllByRestaurant_IdAndOrderStatusIsNotIn(long restaurant_id, Iterable<OrderStatus> orderStatus);

    List<Order> findAllByCity(City city);
}
