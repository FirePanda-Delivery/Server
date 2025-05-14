package ru.diplom.fpd.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import static ru.diplom.fpd.configuration.KafkaConfig.AVG_DELIVERY_TIME_LISTENER_FACTORY;
import ru.diplom.fpd.configuration.properties.DeliveryTimeKafkaProperties;
import ru.diplom.fpd.dto.AverageDeliveryTimeDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryTimeKafkaService {

    private final DeliveryTimeKafkaProperties properties;
    private final RestaurantService restaurantService;

    @KafkaListener(containerFactory = AVG_DELIVERY_TIME_LISTENER_FACTORY,
            topics = "${kafka.delivery-time.in-topic}",
            groupId = "${kafka.delivery-time.group-id}")
    public void listenSearchedMessage(List<AverageDeliveryTimeDto> message) {
        restaurantService.updateRestaurantDeliveryTime(message);
    }

}
