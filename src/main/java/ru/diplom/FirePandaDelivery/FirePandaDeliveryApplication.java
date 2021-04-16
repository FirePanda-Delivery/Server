package ru.diplom.FirePandaDelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import ru.diplom.FirePandaDelivery.Service.OrderServices;

@SpringBootApplication
public class FirePandaDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirePandaDeliveryApplication.class, args);
	}

}
