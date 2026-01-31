package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.dto.CustomerValidationResponseDTO;
import com.alessandragodoy.customerms.service.ICustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link InternalCustomerController}.
 */

@WebMvcTest(InternalCustomerController.class)
class InternalCustomerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	ICustomerService customerService;

	@Test
	@DisplayName("GET /validate-customer/{customerId} - returns valid response when customer " +
			"exists and is active")
	void validateCustomer_WithActiveCustomer_ReturnsValidResponse() throws Exception {

		Integer customerId = 1;
		CustomerValidationResponseDTO validResponse = createValidResponse();

		when(customerService.validateCustomer(customerId)).thenReturn(validResponse);

		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}",
						customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.exists").value(true))
				.andExpect(jsonPath("$.isActive").value(true))
				.andDo(print());

		verify(customerService).validateCustomer(customerId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - returns invalid response when customer " +
			"does not exist")
	void validateCustomer_WithNonExistentCustomer_ReturnsInvalidResponse() throws Exception {

		Integer nonExistentId = 999;
		String expectedMessage = "Customer not found for ID: " + nonExistentId;
		CustomerValidationResponseDTO invalidResponse = createInvalidResponse(expectedMessage);

		when(customerService.validateCustomer(nonExistentId)).thenReturn(invalidResponse);

		mockMvc.perform(
						get("/api/v1/internal/customers/validate-customer/{customerId}",
								nonExistentId)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.exists").value(false))
				.andExpect(jsonPath("$.message").value(expectedMessage))
				.andDo(print());

		verify(customerService).validateCustomer(nonExistentId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - returns inactive response when customer " +
			"exists but is not active")
	void validateCustomer_WithInactiveCustomer_ReturnsInactiveResponse() throws Exception {

		Integer inactiveCustomerId = 5;
		String expectedMessage = "Customer is not active for ID: " + inactiveCustomerId;
		CustomerValidationResponseDTO inactiveResponse = createInactiveResponse(expectedMessage);

		when(customerService.validateCustomer(inactiveCustomerId)).thenReturn(inactiveResponse);

		mockMvc.perform(
						get("/api/v1/internal/customers/validate-customer/{customerId}",
								inactiveCustomerId)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.exists").value(true))
				.andExpect(jsonPath("$.isActive").value(false))
				.andExpect(jsonPath("$.message").value(expectedMessage))
				.andDo(print());

		verify(customerService).validateCustomer(inactiveCustomerId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - handles zero as customer ID")
	void validateCustomer_WithZeroId_ReturnsInvalidResponse() throws Exception {

		Integer zeroId = 0;
		String expectedMessage = "Customer not found for ID: " + zeroId;
		CustomerValidationResponseDTO invalidResponse = createInvalidResponse(expectedMessage);

		when(customerService.validateCustomer(zeroId)).thenReturn(invalidResponse);

		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}", zeroId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.exists").value(false))
				.andDo(print());

		verify(customerService).validateCustomer(zeroId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - handles negative customer ID")
	void validateCustomer_WithNegativeId_ReturnsInvalidResponse() throws Exception {

		Integer negativeId = -1;
		String expectedMessage = "Customer not found for ID: " + negativeId;
		CustomerValidationResponseDTO invalidResponse = createInvalidResponse(expectedMessage);

		when(customerService.validateCustomer(negativeId)).thenReturn(invalidResponse);

		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}",
						negativeId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.exists").value(false))
				.andDo(print());

		verify(customerService).validateCustomer(negativeId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - handles very large customer ID")
	void validateCustomer_WithLargeId_ReturnsResponse() throws Exception {

		Integer largeId = Integer.MAX_VALUE;
		String expectedMessage = "Customer not found for ID: " + largeId;
		CustomerValidationResponseDTO invalidResponse = createInvalidResponse(expectedMessage);

		when(customerService.validateCustomer(largeId)).thenReturn(invalidResponse);

		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}", largeId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

		verify(customerService).validateCustomer(largeId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - verifies response structure for valid " +
			"customer")
	void validateCustomer_ValidCustomer_ReturnsCorrectStructure() throws Exception {

		Integer customerId = 10;
		CustomerValidationResponseDTO validResponse = CustomerValidationResponseDTO.valid();

		when(customerService.validateCustomer(customerId)).thenReturn(validResponse);

		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}",
						customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.exists").exists())
				.andExpect(jsonPath("$.isActive").exists())
				.andDo(print());

		verify(customerService).validateCustomer(customerId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - verifies response structure for invalid customer")
	void validateCustomer_InvalidCustomer_ReturnsCorrectStructure() throws Exception {

		Integer customerId = 999;
		String message = "Customer not found for ID: " + customerId;
		CustomerValidationResponseDTO invalidResponse =
				CustomerValidationResponseDTO.invalid(message);

		when(customerService.validateCustomer(customerId)).thenReturn(invalidResponse);

		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}",
						customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.exists").exists())
				.andExpect(jsonPath("$.message").exists())
				.andExpect(jsonPath("$.message").isNotEmpty())
				.andDo(print());

		verify(customerService).validateCustomer(customerId);
	}

	@Test
	@DisplayName("GET /validate-customer/{customerId} - ensures consistent behavior across " +
			"multiple calls")
	void validateCustomer_MultipleCallsSameId_ReturnsConsistentResponse() throws Exception {

		Integer customerId = 1;
		CustomerValidationResponseDTO validResponse = createValidResponse();

		when(customerService.validateCustomer(customerId)).thenReturn(validResponse);

		// First call
		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}",
						customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.exists").value(true))
				.andExpect(jsonPath("$.isActive").value(true));

		// Second call
		mockMvc.perform(get("/api/v1/internal/customers/validate-customer/{customerId}",
						customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.exists").value(true))
				.andExpect(jsonPath("$.isActive").value(true));

		verify(customerService, org.mockito.Mockito.times(2)).validateCustomer(customerId);
	}

	private CustomerValidationResponseDTO createInvalidResponse(String message) {
		return CustomerValidationResponseDTO.invalid(message);
	}

	private CustomerValidationResponseDTO createInactiveResponse(String message) {
		return CustomerValidationResponseDTO.inactive(message);
	}

	private CustomerValidationResponseDTO createValidResponse() {
		return CustomerValidationResponseDTO.valid();
	}
}