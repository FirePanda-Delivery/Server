package ru.diplom.fpd.dto.filter;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantFilterDto {

    private String city;
    private List<String> categories;
    private List<String> products;



}
