package ru.diplom.FirePandaDelivery;

import io.swagger.annotations.SwaggerDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@EnableSwagger2
@SpringBootApplication
public class FirePandaDeliveryApplication {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		SpringApplication.run(FirePandaDeliveryApplication.class, args);
	}

}
