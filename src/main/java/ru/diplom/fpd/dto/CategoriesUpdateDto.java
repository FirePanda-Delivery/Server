package ru.diplom.fpd.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.diplom.fpd.model.Categories;

/**
 * DTO for {@link Categories}
 */
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriesUpdateDto implements Serializable {
    private final long id;
    private final String name;
}