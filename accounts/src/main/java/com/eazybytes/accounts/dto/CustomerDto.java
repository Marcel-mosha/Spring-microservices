package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema for a customer with their account details."
)
public class CustomerDto {

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    @Schema(
            description = "The name of the customer. Must be between 5 and 30 characters.",
            example = "Jack Sparrow"
    )
    private  String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Schema(
            description = "The email address of the customer. Must be a valid email format.",
            example = "jack.sparrow@example.com"
    )
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @Schema(
            description = "The mobile number of the customer. Must be 10 digits.",
            example = "1234567890"
    )
    private String mobileNumber;

    @Schema(
            description = "The account details of the customer."
    )
    private AccountsDto accountsDto;
}
