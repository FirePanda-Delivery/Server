package ru.diplom.fpd.processing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import ru.diplom.fpd.dto.Coordinates;

public class MoneyProcessing {

    @Value("${delivery.cost.coefficient}")
    private double coefficient;

    WebClient webClient = WebClient.create("https://geocode-maps.yandex.ru/1.x");

    @Value("${yandex.api.key}")
    String apiKey;


    public double getCostDelivery(String restAddress, String address) {

        Coordinates addressCords = getCords(address);
        Coordinates coordinates = getCords(restAddress);

        return Math.sqrt(Math.pow((addressCords.getX() - coordinates.getX()), 2) +
                Math.pow((addressCords.getY() - coordinates.getY()), 2)) *
                coefficient;
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
