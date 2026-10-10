package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Schema for an error response with details about the error."
)
public class ErrorResponseDto {
    @Schema(
            description = "The API path that caused the error."
    )
    private String apiPath;


    @Schema(
            description = "The HTTP status code of the error."
    )
    private HttpStatus errorCode;


    @Schema(
            description = "The error message."
    )
    private String errorMessage;


    @Schema(
            description = "The time the error occurred."
    )
    private LocalDateTime errorTime;
}
