package com.eazybytes.accounts.dto;

import com.easybank.cards.dto.CardsDto;
import com.easybank.loans.dto.LoansDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(
        name = "CustomerDetails",
        description = "Schema to hold customer details including accounts, loans, and cards information.")

public class CustomerDetailsDto {

    @Schema(description = "Name of the customer", example = "Jack Sparrow")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")
    private String name;



    @Schema(description = "Email of the customer", example = "jack.sparrow@example.com")
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;


    @Schema(description = "Mobile number of the customer", example = "0723456789")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    @Schema(description = "Account details of the customer")
    private AccountsDto accountsDto;


    @Schema(description = "Loans details of the customer")
    private LoansDto loansDto;

    @Schema(description = "Cards details of the customer")
    private CardsDto cardsDto;


}
