package ru.diplom.fpd.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;
import ru.diplom.fpd.dto.yandex.GeoObject;


public class YandexGeoResponseDeserializer extends StdDeserializer<List<GeoObject>> {

    protected YandexGeoResponseDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    protected YandexGeoResponseDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected YandexGeoResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<GeoObject> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        ArrayNode root = node.get("response").get("GeoObjectCollection").withArray("featureMember");


        return StreamSupport.stream(root.spliterator(), false)
                .map(item -> item.get("GeoObject"))
                .map(item -> GeoObject.builder()
                        .name(item.get("name").asText())
                        .description(item.get("description").asText())
                        .point(item.get("Point").get("pos").asText())
                        .build()
                ).toList();

    }
}
