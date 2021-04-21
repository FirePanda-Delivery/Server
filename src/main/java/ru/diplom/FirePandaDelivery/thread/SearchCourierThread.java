package ru.diplom.FirePandaDelivery.thread;

import lombok.Data;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.diplom.FirePandaDelivery.dto.ActiveCourier;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Order;
import ru.diplom.FirePandaDelivery.processing.AddressProcessing;
import ru.diplom.FirePandaDelivery.service.CourierService;
import ru.diplom.FirePandaDelivery.service.OrderServices;

import java.util.List;

@Setter
public class SearchCourierThread extends Thread {

    private Order order;
    private Cities city;
    private AddressProcessing addressProcessing;
    private CourierService courierService;
    private OrderServices orderServices;

    public SearchCourierThread(Order order, Cities city, AddressProcessing addressProcessing, CourierService courierService, OrderServices orderServices) {
        this.order = order;
        this.city = city;
        this.addressProcessing = addressProcessing;
        this.courierService = courierService;
        this.orderServices = orderServices;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCity(Cities city) {
        this.city = city;
    }

    @SneakyThrows
    @Override
    public void run() {

        super.run();

        boolean complete = true;

        do {
            sleep(60000);

            List<ActiveCourier> activeCouriers = courierService.getActiveCourierByCity(city.getCiti());

            if (activeCouriers == null || activeCouriers.isEmpty()) {
                complete = true;
                continue;
            }

//          order.setCourier(addressProcessing.courierNearestToAddress(activeCouriers, city.getCiti()));
            orderServices.addCourier(order, addressProcessing.courierNearestToAddress(activeCouriers, city.getCiti()));
            complete = false;

        } while (complete);
    }
}
