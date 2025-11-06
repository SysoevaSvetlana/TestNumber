package com.example.test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Number Sorting Microservice API")
                        .version("1.0.0")
                        .description("""
                                REST API для сохранения чисел в базу данных и получения отсортированного списка.
                                
                                ## Функциональность
                                - Принимает одно число через POST запрос
                                - Сохраняет число в PostgreSQL
                                - Возвращает отсортированный список всех чисел
                                
                                ## Пример использования
                                1. POST 3 → Ответ: [3]
                                2. POST 2 → Ответ: [2, 3]
                                3. POST 1 → Ответ: [1, 2, 3]
                                """)
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }
}

