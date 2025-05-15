package ru.diplom.fpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO for {@link ru.diplom.fpd.model.Product}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto implements Serializable {
    private final long id;
    private final String name;
    private final String description;
    private final int price;
    private final int weight;
    private final float popularity;
    private final String img;
}