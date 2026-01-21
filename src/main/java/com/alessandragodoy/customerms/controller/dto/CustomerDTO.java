package com.alessandragodoy.customerms.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Customer.
 *
 * @param customerId     the ID of the customer
 * @param firstName      the first name of the customer
 * @param lastName       the last name of the customer
 * @param documentNumber the DNI of the customer
 * @param email          the email of the customer
 * @param phoneNumber    the phone number of the customer
 * @param address        the address of the customer
 */
public record CustomerDTO(
		@Schema(description = "Unique identifier for the customer", example = "1")
		Integer customerId,
		@NotNull
		@Size(max = 30, message = "First name length must not exceed 30 characters")
		@Schema(description = "First name of the customer", example = "Jane")
		String firstName,
		@NotNull
		@Size(max = 30, message = "Last name length must not exceed 30 characters")
		@Schema(description = "Last name of the customer", example = "Doe")
		String lastName,
		@NotNull
		@Size(max = 15, message = "Document number length must not exceed 15 characters")
		@Schema(description = "Costumer's document number", example = "12345678")
		String documentNumber,
		@NotNull
		@Email
		@Size(max = 50, message = "Email length must not exceed 50 characters")
		@Schema(description = "Email address of the customer", example = "jdoe@mail.com")
		String email,
		@NotNull
		@Pattern(regexp = "\\d{9,12}", message = "Phone number must contain only digits")
		@Size(max = 12, message = "Phone number length must not exceed 12 characters")
		@Schema(description = "Phone number of the customer", example = "51999999999")
		String phoneNumber,
		@NotNull
		@Size(max = 150, message = "Address length must not exceed 150 characters")
		@Schema(description = "Address of the customer", example = "123 Main St, Lima, Peru")
		String address
) {
}
