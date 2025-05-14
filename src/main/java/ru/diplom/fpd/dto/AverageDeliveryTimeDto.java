package ru.diplom.fpd.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AverageDeliveryTimeDto implements Serializable {
    Long restaurantId;
    String city;
    Short deliveryTime;
}