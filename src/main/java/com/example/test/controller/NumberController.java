package com.example.test.controller;

import com.example.test.dto.ErrorResponse;
import com.example.test.service.NumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/numbers")
@RequiredArgsConstructor
@Tag(name = "Numbers", description = "API для работы с числами")
public class NumberController {

    private final NumberService numberService;

    @Operation(
            summary = "Добавить число и получить отсортированный список",
            description = """
                    Принимает одно число, сохраняет его в базу данных и возвращает
                    отсортированный список всех сохраненных чисел.

                    Примеры:
                    - Отправили 3 → Вернули [3]
                    - Отправили 2 → Вернули [2, 3]
                    - Отправили 1 → Вернули [1, 2, 3]

                    Валидация:
                    - Число не может быть null
                    - Должно быть целым числом (Integer)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Число успешно добавлено, возвращен отсортированный список",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = @ExampleObject(
                                    name = "Пример ответа",
                                    value = "[1, 2, 3, 5, 10]",
                                    description = "Отсортированный список всех чисел в базе данных"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации или неверный формат данных",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Ошибка валидации",
                                    value = """
                                            {
                                              "error": "VALIDATION_ERROR",
                                              "message": "Число не может быть null",
                                              "timestamp": "2025-11-06T22:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<List<Integer>> addNumber(
            @Parameter(
                    description = "Число для добавления в базу данных",
                    required = true,
                    example = "42",
                    schema = @Schema(type = "integer", format = "int32")
            )
            @Valid
            @NotNull(message = "Число не может быть null")
            @RequestBody Integer number
    ) {
        List<Integer> sortedNumbers = numberService.addNumberAndGetSorted(number);
        return ResponseEntity.ok(sortedNumbers);
    }
}

