package ru.diplom.fpd.thread;

import java.util.List;
import lombok.Setter;
import lombok.SneakyThrows;
import ru.diplom.fpd.dto.ActiveCourier;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.Order;
import ru.diplom.fpd.processing.AddressProcessing;
import ru.diplom.fpd.service.CourierService;
import ru.diplom.fpd.service.OrderServices;

@Setter
public class SearchCourierThread extends Thread {

    private Order order;
    private City city;
    private AddressProcessing addressProcessing;
    private CourierService courierService;
    private OrderServices orderServices;

    public SearchCourierThread(Order order, City city, AddressProcessing addressProcessing, CourierService courierService, OrderServices orderServices) {
        this.order = order;
        this.city = city;
        this.addressProcessing = addressProcessing;
        this.courierService = courierService;
        this.orderServices = orderServices;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @SneakyThrows
    @Override
    public void run() {

        super.run();

        boolean complete = false;

        do {
            sleep(60000);

            List<ActiveCourier> activeCouriers = courierService.getActiveCourierByCity(city.getCity());

            if (activeCouriers == null || activeCouriers.isEmpty()) {
                complete = true;
                continue;
            }

//          order.setCourier(addressProcessing.courierNearestToAddress(activeCouriers, city.getCiti()));
            orderServices.addCourier(order, addressProcessing.courierNearestToAddress(activeCouriers, city.getCity()));
            complete = true;

        } while (!complete);
    }
}
