package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(
        name = "Accounts",
        description = "Schema for a customer's account details."
)
public class AccountsDto {

    @NotEmpty(message = "Account number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{12})", message = "Account number must be 12 digits")
    @Schema(
            description = "The account number of the customer. Must be 12 digits.",
            example = "123456789012"
    )
    private Long accountNumber;

    @NotEmpty(message = "Account type cannot be empty")
    @Schema(
            description = "The type of the account.",
            example = "Savings"
    )
    private String accountType;

    @NotEmpty(message = "Branch address cannot be empty")
    @Schema(
            description = "The address of the branch where the account is held.",
            example = "123 Main St, Springfield, IL 62701"
    )
    private String branchAddress;
}
