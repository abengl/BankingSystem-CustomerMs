package com.alessandragodoy.customerms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for customer validation responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerValidationResponseDTO {

	@Schema(description = "Indicates if the customer exists", example = "true")
	private Boolean exists;

	@Schema(description = "Indicates if the customer is active", example = "true")
	private Boolean isActive;

	@Schema(description = "Message providing additional information about the customer validation"
			, example = "Customer is inactive")
	private String message;

	public static CustomerValidationResponseDTO valid() {
		return CustomerValidationResponseDTO.builder()
				.exists(true)
				.isActive(true)
				.build();
	}

	public static CustomerValidationResponseDTO invalid(String reason) {
		return CustomerValidationResponseDTO.builder()
				.exists(false)
				.isActive(null)
				.message(reason)
				.build();
	}
	public static CustomerValidationResponseDTO inactive(String reason) {
		return CustomerValidationResponseDTO.builder()
				.exists(true)
				.isActive(false)
				.message(reason)
				.build();
	}
}
