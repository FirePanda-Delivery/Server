package ru.diplom.FirePandaDelivery.dto.requestModel;

import lombok.Data;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@Data
public class RestaurantReq {

    private String name;

    private String description;

    private Time workingHoursStart;

    private Time workingHoursEnd;

    private double minPrice;

    private boolean ownDelivery;

    private List<Map<String, String>> citiesAddress;

}
