package ru.diplom.fpd.processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.diplom.fpd.dto.ActiveCourier;
import ru.diplom.fpd.service.CitiesServices;
import ru.diplom.fpd.dto.Coordinates;
import ru.diplom.fpd.model.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Component
public class AddressProcessing {

    final CitiesServices citiesServices;

    WebClient webClient = WebClient.create("https://geocode-maps.yandex.ru/1.x");

    @Value("${yandex.api.key}")
    String apiKey;

    @Autowired
    public AddressProcessing(CitiesServices citiesServices) {
        this.citiesServices = citiesServices;
    }

    public boolean isValid(String address, String city) {

        return isValid(address, citiesServices.getByName(city));
    }



    public boolean isValid(String address, Cities city) {

        Coordinates cords = getCords(address);
        List<Coordinates> list = new LinkedList<>();

        for (CitiesCoordinates cord : city.getCords()) {
            list.add(new Coordinates(cord.getX(), cord.getY()));
        }
        return horizontalTracing(list, cords);
    }

    public String restaurantNearestToAddress(Restaurant restaurant, String city, String address) {

        if (restaurant == null) {
            throw new NullPointerException();
        }

        if (address == null || address.isEmpty()) {
            throw new NullPointerException();
        }

        Coordinates addressCords = getCords(address);

        int restaurantAddressIndexWithMinimumDistanceToAddress = 0;

        double minDistance = 0;

        List<RestaurantAddress> restaurantAddress = new LinkedList<>();

        for (RestaurantAddress citiesAddress : restaurant.getCitiesAddress()) {
            if (citiesAddress.getCity().getNormalizedCiti().equals(city.toUpperCase(Locale.ROOT))) {
                restaurantAddress.add(citiesAddress);
            }
        }

        if (restaurantAddress.size()<= 1) {
            return restaurantAddress.get(0).getAddress();
        }

        for (int index = 0; index < restaurantAddress.size(); index++) {

            Coordinates coordinates = getCords(restaurantAddress.get(index).getAddress());

            if (index == 0) {
                minDistance = Math.sqrt(Math.pow((addressCords.getX() - coordinates.getX()), 2) +
                        Math.pow((addressCords.getY() - coordinates.getY()), 2));
            }


            double distance = Math.sqrt(Math.pow((addressCords.getX() - coordinates.getX()), 2) +
                    Math.pow((addressCords.getY() - coordinates.getY()), 2));

            if (minDistance > distance) {

                minDistance = distance;
                restaurantAddressIndexWithMinimumDistanceToAddress = index;
            }

        }
        return restaurantAddress.get(restaurantAddressIndexWithMinimumDistanceToAddress).getAddress();
    }

    public Courier courierNearestToAddress(List<ActiveCourier> couriers, String address) {

       Coordinates cords = getCords(address);

        int courierIndexWithMinimumDistanceToAddress = 0;

        double minDistance = 0;

        for (int index = 0; index < couriers.size(); index++) {

            Coordinates courierLocation = couriers.get(index).getLocation();

            if (index == 0) {
                minDistance = Math.sqrt(Math.pow((cords.getX() - courierLocation.getX()), 2) +
                        Math.pow((cords.getY() - courierLocation.getY()), 2));
            }

            double distance = Math.sqrt(Math.pow((cords.getX() - courierLocation.getX()), 2) +
                    Math.pow((cords.getY() - courierLocation.getY()), 2));

            if (minDistance > distance) {

                minDistance = distance;
                courierIndexWithMinimumDistanceToAddress = index;
            }

        }

        return couriers.get(courierIndexWithMinimumDistanceToAddress).getCourier();
    }




    private boolean horizontalTracing(List<Coordinates> cords, Coordinates objectCords) {

        int numberIntersections = 0;

        int size = cords.size();
        double x = objectCords.getX();
        double y = objectCords.getY();

        for (int i = 0, j = 1; i < size; i++, j = i == size - 1 ? 0 : j+1) {

            double x1 = cords.get(i).getX(); // координаты х начала отрезка
            double y1 = cords.get(i).getY(); // координата y начала отрезка

            double x2 = cords.get(j).getX(); // координаты х конца отрезка
            double y2 = cords.get(j).getY(); // координата y конца отрезка


            if ((x-x1) * (y2-y1) - (y-y1) * (x2-x1) == 0){
                if (((x1 < x2) && (x1 <= x) && (x <= x2))
                        || ((x1 > x2) && (x2 <= x) && (x <= x1))
                        || ((x1 == x2) && (y1 < y2) && (y1 < y) && (y < y2))
                        || (x1 == x2) && (y2 < y1) && (y2 < y) && (y < y1)) {

                    return true;
                }
            }

            if ((y-y1) * (y-y2) < 0) {
                if (x<((y-y1)*(x2-x1)/(y2-y1)+x1)) {
                    numberIntersections++;
                }

            } else if (y1 == y2 && y1 == y) {

                Coordinates lastStartCords; // вершина начала предыдущего отрезка
                Coordinates nextEndCords; // вершина конца следуюшего отрезка

                if (i == 0) { lastStartCords = cords.get(size-1); }
                else { lastStartCords = cords.get(i - 1); }

                if(i==size-2) { nextEndCords = cords.get(1); }
                else { nextEndCords = cords.get(j + 1); }

                if((y - lastStartCords.getY()) * (y - nextEndCords.getY()) < 0) {
                    numberIntersections++;
                } else {
                    numberIntersections += 2;
                }

            } else if (y == y1 && x1 > x) {

                Coordinates lastCords; // предыдущая вершина

                if (i == 1) { lastCords = cords.get(size-1); }
                else { lastCords = cords.get(i - 1); }

                if ((y - lastCords.getY()) * (y - y2) < 0) {
                    numberIntersections ++;
                } else {
                    numberIntersections += 2;
                }
            }
        }

        return numberIntersections % 2 != 0;
    }


    public final Coordinates getCords(String address) {

        if (address == null || address.isEmpty()) {
            throw new NullPointerException("address not set");
        }

        String result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("format", "json")
                        .queryParam("results", 1)
                        .queryParam("geocode", address)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (result == null || result.isEmpty()) {
            throw new NullPointerException("the result of a query to the yandex geocoder api is null");
        }


        int beginIndex = result.indexOf('"', result.indexOf("\"pos\":") + 6) + 1;
        int endIndex = result.indexOf('"', beginIndex);

        return Coordinates.toCoordinates(result.substring(beginIndex, endIndex));
    }

}


