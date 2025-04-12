package ru.diplom.FirePandaDelivery.dto.requestModel;

import lombok.Data;

import java.sql.Time;

@Data
public class UpdateRestaurantReq {

    private long id;

    private String name;

    private String normalizedName;

    private String description;

    private Time workingHoursStart;

    private Time workingHoursEnd;

    private double minPrice;

    private boolean ownDelivery;

}
