package com.example.test.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией об ошибке")
public class ErrorResponse {
    
    @Schema(description = "Код ошибки", example = "VALIDATION_ERROR")
    private String error;
    
    @Schema(description = "Сообщение об ошибке", example = "Число не может быть null")
    private String message;
    
    @Schema(description = "Временная метка ошибки", example = "2025-11-06T22:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

