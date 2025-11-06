package com.example.test.controller;

import com.example.test.repository.NumberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NumberControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NumberRepository numberRepository;

    @BeforeEach
    void setUp() {
        numberRepository.deleteAll();
    }

    @Test
    void addNumber_shouldReturnSingleElementArray_whenFirstNumber() throws Exception {
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(3)));
    }

    @Test
    void addNumber_shouldReturnSortedArray_whenMultipleNumbers() throws Exception {
        // Добавляем первое число
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(3)));

        // Добавляем второе число
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(2)))
                .andExpect(jsonPath("$[1]", is(3)));

        // Добавляем третье число
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is(1)))
                .andExpect(jsonPath("$[1]", is(2)))
                .andExpect(jsonPath("$[2]", is(3)));
    }

    @Test
    void addNumber_shouldHandleNegativeNumbers() throws Exception {
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("-3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(-3)))
                .andExpect(jsonPath("$[1]", is(5)));
    }

    @Test
    void addNumber_shouldHandleDuplicateNumbers() throws Exception {
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(5)))
                .andExpect(jsonPath("$[1]", is(5)));
    }

    @Test
    void addNumber_shouldMaintainSortOrderWithRandomNumbers() throws Exception {
        int[] numbers = {10, 3, 7, 1, 9, 2};
        
        for (int i = 0; i < numbers.length; i++) {
            mockMvc.perform(post("/api/numbers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(String.valueOf(numbers[i])))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(i + 1)));
        }

        // Проверяем финальный отсортированный список
        mockMvc.perform(post("/api/numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0]", is(1)))
                .andExpect(jsonPath("$[1]", is(2)))
                .andExpect(jsonPath("$[2]", is(3)))
                .andExpect(jsonPath("$[3]", is(5)))
                .andExpect(jsonPath("$[4]", is(7)))
                .andExpect(jsonPath("$[5]", is(9)))
                .andExpect(jsonPath("$[6]", is(10)));
    }
}

