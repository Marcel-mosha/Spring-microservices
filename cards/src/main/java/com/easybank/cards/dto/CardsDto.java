package com.easybank.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Schema(
        name = "Cards",
        description = "Schema for a customer's card details."
)
@Data
public class CardsDto {

    @NotEmpty(message = "Mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @Schema(
            description = "The mobile number of the customer. Must be 10 digits.",
            example = "0723456789"
    )
    private String mobileNumber;

    @NotEmpty(message = "Card number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{12})", message = "Card number must be 12 digits")
    @Schema(
            description = "The card number of the customer. Must be 12 digits.",
            example = "123456789012"
    )
    private String cardNumber;


    @NotEmpty(message = "Card type cannot be empty")
    @Schema(
            description = "The type of the card.",
            example = "Credit Card"
    )
    private String cardType;


    @Positive(message = "Total limit must be a positive number")
    @Schema(
            description = "The total limit of the card. Must be a positive number.",
            example = "5000"
    )
    private int totalLimit;

    @PositiveOrZero(message = "Amount used must be zero or a positive number")
    @Schema(
            description = "The amount of money used on the card. Must be zero or a positive number.",
            example = "2500"
    )
    private int amountUsed;

    @PositiveOrZero(message = "Available amount must be zero or a positive number")
    @Schema(
            description = "The available amount on the card. Must be zero or a positive number.",
            example = "2500"
    )
    private int availableAmount;
}
