package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema for a response with a status code and message."
)
public class ResponseDto {
    @Schema(
            description = "The status code of the response."
    )
    private String statusCode;


    @Schema(
            description = "The status message of the response."
    )
    private String statusMsg;
}
