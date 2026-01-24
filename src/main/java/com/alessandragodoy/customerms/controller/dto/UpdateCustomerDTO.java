package com.alessandragodoy.customerms.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateCustomerDTO {

	@Email
	@Size(max = 50, message = "Email length must not exceed 50 characters")
	@Schema(description = "Email address of the customer", example = "jdoe@mail.com")
	private String email;

	@Pattern(regexp = "\\d{9,12}", message = "Phone number must contain only digits")
	@Size(max = 12, message = "Phone number length must not exceed 12 characters")
	@Schema(description = "Phone number of the customer", example = "51999999999")
	private String phoneNumber;

	@Size(max = 150, message = "Address length must not exceed 150 characters")
	@Schema(description = "Address of the customer", example = "123 Main St, Lima, Peru")
	private String address;
}
