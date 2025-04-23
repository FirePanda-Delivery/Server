package ru.diplom.fpd.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.diplom.fpd.dto.ActiveCourier;
import ru.diplom.fpd.dto.Coordinates;
import ru.diplom.fpd.dto.yandex.GeoObject;
import ru.diplom.fpd.dto.yandex.GeoObjectCollection;
import ru.diplom.fpd.model.CitiesCoordinates;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.Courier;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;
import ru.diplom.fpd.service.CitiesServices;

@Component
@RequiredArgsConstructor()
public class AddressProcessing {

    private final CitiesServices citiesServices;

    @Qualifier("yandexMapsRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${yandex.api.key}")
    String apiKey;

    public boolean isValid(String address, String city) {

        return isValid(address, citiesServices.getByName(city));
    }


    public boolean isValid(String address, City city) {

        Coordinates cords = getCords(address);
        List<Coordinates> list = new LinkedList<>();

        for (CitiesCoordinates cord : city.getCoordinates()) {
            list.add(new Coordinates(cord.getX(), cord.getY()));
        }
        return horizontalTracing(list, cords);
    }

    public RestaurantAddress restaurantNearestToAddress(Restaurant restaurant, String city, String address) {

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
            if (citiesAddress.getCity().getCity().toUpperCase().equals(city.toUpperCase(Locale.ROOT))) {
                restaurantAddress.add(citiesAddress);
            }
        }

        if (restaurantAddress.size() <= 1) {
            return restaurantAddress.get(0);
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
        return restaurantAddress.get(restaurantAddressIndexWithMinimumDistanceToAddress);
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

        for (int i = 0, j = 1; i < size; i++, j = i == size - 1 ? 0 : j + 1) {

            double x1 = cords.get(i).getX(); // координаты х начала отрезка
            double y1 = cords.get(i).getY(); // координата y начала отрезка

            double x2 = cords.get(j).getX(); // координаты х конца отрезка
            double y2 = cords.get(j).getY(); // координата y конца отрезка


            if ((x - x1) * (y2 - y1) - (y - y1) * (x2 - x1) == 0) {
                if (((x1 < x2) && (x1 <= x) && (x <= x2))
                        || ((x1 > x2) && (x2 <= x) && (x <= x1))
                        || ((x1 == x2) && (y1 < y2) && (y1 < y) && (y < y2))
                        || (x1 == x2) && (y2 < y1) && (y2 < y) && (y < y1)) {

                    return true;
                }
            }

            if ((y - y1) * (y - y2) < 0) {
                if (x < ((y - y1) * (x2 - x1) / (y2 - y1) + x1)) {
                    numberIntersections++;
                }

            } else if (y1 == y2 && y1 == y) {

                Coordinates lastStartCords; // вершина начала предыдущего отрезка
                Coordinates nextEndCords; // вершина конца следуюшего отрезка

                if (i == 0) {
                    lastStartCords = cords.get(size - 1);
                } else {
                    lastStartCords = cords.get(i - 1);
                }

                if (i == size - 2) {
                    nextEndCords = cords.get(1);
                } else {
                    nextEndCords = cords.get(j + 1);
                }

                if ((y - lastStartCords.getY()) * (y - nextEndCords.getY()) < 0) {
                    numberIntersections++;
                } else {
                    numberIntersections += 2;
                }

            } else if (y == y1 && x1 > x) {

                Coordinates lastCords; // предыдущая вершина

                if (i == 1) {
                    lastCords = cords.get(size - 1);
                } else {
                    lastCords = cords.get(i - 1);
                }

                if ((y - lastCords.getY()) * (y - y2) < 0) {
                    numberIntersections++;
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

        String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://geocode-maps.yandex.ru/1.x")
                .queryParam("apikey", "{apikey}")
                .queryParam("format", "{format}")
                .queryParam("results", "{results}")
                .queryParam("geocode", "{geocode}")
                .encode()
                .toUriString();

        Map<String, Object> params = Map.of(
                "apikey", apiKey,
                "format", "json",
                "results", 1,
                "geocode", address);

        List<GeoObject> result = restTemplate
                .exchange(urlTemplate, HttpMethod.GET, null,
                        GeoObjectCollection.class,
                        params)
                .getBody()
                .getGeoObjects();

        return Optional.ofNullable(result)
                .filter(Predicate.not(List::isEmpty))
                .map(list -> list.get(0))
                .map(GeoObject::getPoint)
                .map(Coordinates::toCoordinates)
                .orElseThrow();
    }

}


