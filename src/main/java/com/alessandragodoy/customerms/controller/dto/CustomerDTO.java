package com.alessandragodoy.customerms.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Customer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {

	@Schema(description = "Unique identifier for the customer", example = "1")
	Integer customerId;

	@NotBlank(message = "First name cannot be empty")
	@Size(max = 30, message = "First name length must not exceed 30 characters")
	@Schema(description = "First name of the customer", example = "Jane")
	String firstName;

	@NotBlank(message = "Last name cannot be empty")
	@Size(max = 30, message = "Last name length must not exceed 30 characters")
	@Schema(description = "Last name of the customer", example = "Doe")
	String lastName;

	@NotBlank(message = "Document type cannot be empty")
	@Size(max = 15, message = "Document number length must not exceed 15 characters")
	@Schema(description = "Costumer's document number", example = "12345678")
	String documentNumber;

	@NotBlank(message = "Email cannot be empty")
	@Email
	@Size(max = 50, message = "Email length must not exceed 50 characters")
	@Schema(description = "Email address of the customer", example = "jdoe@mail.com")
	String email;

	@NotBlank(message = "Phone number cannot be empty")
	@Pattern(regexp = "\\d{9,12}", message = "Phone number must contain only digits")
	@Size(max = 12, message = "Phone number length must not exceed 12 characters")
	@Schema(description = "Phone number of the customer", example = "51999999999")
	String phoneNumber;

	@NotBlank(message = "Address cannot be empty")
	@Size(max = 150, message = "Address length must not exceed 150 characters")
	@Schema(description = "Address of the customer", example = "123 Main St, Lima, Peru")
	String address;

}
