package com.easybank.cards.controller;


import com.easybank.cards.constants.CardsConstants;
import com.easybank.cards.dto.CardsContactInfoDto;
import com.easybank.cards.dto.CardsDto;
import com.easybank.cards.dto.ErrorResponseDto;
import com.easybank.cards.dto.ResponseDto;
import com.easybank.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Cards Microservice",
        description = "CRUD REST APIs in EasyBank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CardsController {
    private final ICardsService iCardsService;

    public CardsController(ICardsService iCardsService) {
        this.iCardsService = iCardsService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;


    @PostMapping("/createCard")
    @Operation(
            summary = "Create a new card for a customer",
            description = "This API creates a new card for a customer based on the provided mobile number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
            )
    })
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam
                                                  @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                  String mobileNumber) {
        iCardsService.createCard(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }



    @GetMapping("/fetchCard")
    @Operation(
            summary = "Fetch card details for a customer",
            description = "This API fetches the card details for a customer based on the provided mobile number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card details fetched successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam
                                                     @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                     String mobileNumber) {
        CardsDto cardsDto = iCardsService.getCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }



    @PutMapping("/updateCard")
    @Operation(
            summary = "Update card details for a customer",
            description = "This API updates the card details for a customer based on the provided card information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card details updated successfully"),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed - Card details could not be updated"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
        boolean isUpdated = iCardsService.updateCard(cardsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }




    @DeleteMapping("/deleteCard")
    @Operation(
            summary = "Delete card details for a customer",
            description = "This API deletes the card details for a customer based on the provided mobile number."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card details deleted successfully"),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed - Card details could not be deleted"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber) {
        boolean isDeleted = iCardsService.deleteCard(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping("/build-version")
    @Operation(
            summary = "Get build version",
            description = "This endpoint allows you to retrieve the current build version of the application."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Build version retrieved successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while retrieving build version",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }



    @GetMapping("/java-version")
    @Operation(
            summary = "Get Java version",
            description = "This endpoint allows you to retrieve the current Java version of the application."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Java version retrieved successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while retrieving Java version",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("java.version"));
    }


    @GetMapping("/contact-info")
    @Operation(
            summary = "Get contact information",
            description = "This endpoint allows you to retrieve the contact information for the application."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contact information retrieved successfully"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error while retrieving contact information",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<CardsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }

}
