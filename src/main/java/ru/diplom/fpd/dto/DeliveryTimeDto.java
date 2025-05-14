package ru.diplom.fpd.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTimeDto {

    private short allCitiesAvgDeliveryTime;
    private List<AverageDeliveryTimeDto> averageDeliveryTimes;

}
