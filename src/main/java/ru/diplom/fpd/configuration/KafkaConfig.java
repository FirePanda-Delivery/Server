package ru.diplom.fpd.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import java.util.List;
import java.util.Map;
import javax.lang.model.type.ReferenceType;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.diplom.fpd.dto.AverageDeliveryTimeDto;

@Configuration
@RequiredArgsConstructor
@EnableKafka
public class KafkaConfig {

    public static final String AVG_DELIVERY_TIME_CONSUMER_FACTORY_BEAN_NAME = "avgDeliveryTimeConsumerFactory";
    public static final String AVG_DELIVERY_TIME_LISTENER_FACTORY = "avgDeliveryTimeListenerFactory";

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = kafkaProperties.buildProducerProperties();
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean(AVG_DELIVERY_TIME_CONSUMER_FACTORY_BEAN_NAME)
    public ConsumerFactory<String, List<AverageDeliveryTimeDto>> courierFoundConsumerFactory() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        TypeReference<List<AverageDeliveryTimeDto>> typeReference = new TypeReference<>(){};
        JsonDeserializer<List<AverageDeliveryTimeDto>> deserializer = new JsonDeserializer<>(typeReference, false);
        deserializer.trustedPackages("*");
        deserializer.ignoreTypeHeaders();
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new ErrorHandlingDeserializer<>(deserializer));
    }

    @Bean(AVG_DELIVERY_TIME_LISTENER_FACTORY)
    public ConcurrentKafkaListenerContainerFactory<String, List<AverageDeliveryTimeDto>> courierFoundListenerContainerFactory(
            @Qualifier(AVG_DELIVERY_TIME_CONSUMER_FACTORY_BEAN_NAME)
            ConsumerFactory<String, List<AverageDeliveryTimeDto>> consumerFactory
    ) {

        ConcurrentKafkaListenerContainerFactory<String, List<AverageDeliveryTimeDto>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
