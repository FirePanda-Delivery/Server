package ru.diplom.fpd.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@OpenAPIDefinition(
        servers = {
                @Server(url = "/", description = "Default Server URL")
        }
)
@Configuration
public class ApplicationConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean(name = "yandexMapsRestTemplate")
    RestTemplate createYMapsRestTemplate() {
        return new RestTemplateBuilder()
                .build();

    }

}