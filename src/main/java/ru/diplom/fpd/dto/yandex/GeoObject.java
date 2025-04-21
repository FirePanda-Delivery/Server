package ru.diplom.fpd.dto.yandex;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.diplom.fpd.deserializers.YandexGeoResponseDeserializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonDeserialize(using = YandexGeoResponseDeserializer.class)
public class GeoObject {

    private String name;
    private String description;
    private String point = ": { pos\": \"39.218487 51.68683";

}
