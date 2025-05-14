package ru.diplom.fpd.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka.delivery-time")
public class DeliveryTimeKafkaProperties {
    private String inTopic;
    private String groupId;
}
