package com.eazybytes.accounts.controller;


import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.dto.ErrorResponseDto;
import com.eazybytes.accounts.dto.ResponseDto;
import com.eazybytes.accounts.service.IAccontsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Accounts CRUD Operations",
        description = "This API allows you to perform CRUD operations on customer accounts."
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {
    private IAccontsService iAccontsService;


    @PostMapping("/create")
    @Operation(
            summary = "Create a new customer account",
            description = "This endpoint allows you to create a new customer account by providing the necessary details in the request body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while creating account",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccontsService.creteAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }



    @GetMapping("/fetch")
    @Operation(
            summary = "Fetch customer account details",
            description = "This endpoint allows you to fetch the details of a customer account by providing the mobile number."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Account details fetched successfully"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error while fetching account details",
            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
        )
    })
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        CustomerDto customerDto = iAccontsService.fetchAccount(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);

    }



    @PutMapping("/update")
    @Operation(
            summary = "Update customer account details",
            description = "This endpoint allows you to update the details of a customer account by providing the updated information in the request body."
    )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Account details updated successfully"),
           @ApiResponse(
                   responseCode = "500",
                   description = "Internal server error while updating account details",
                   content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
           @ApiResponse(
                   responseCode = "417",
                   description = "Account update failed"
           )
    })
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto){
        boolean isUpdated = iAccontsService.updateAccount(customerDto);
        if(isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }



    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete customer account details",
            description = "This endpoint allows you to delete the details of a customer account by providing the mobile number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account details deleted successfully"),
            @ApiResponse(responseCode = "417", description = "Account deletion failed"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while deleting account details",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccontsService.deleteAccount(mobileNumber);
        if(isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }
}