package com.alessandragodoy.customerms.controller;

import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.controller.dto.UpdateCustomerDTO;
import com.alessandragodoy.customerms.exception.CustomerNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.service.ICustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit test for {@link CustomerController}
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockitoBean
	ICustomerService customerService;

	@Test
	@DisplayName("GET /api/v1/customers - returns list of active customers")
	void getAllCustomers_WithActiveCustomers_ReturnsCustomerDTOList() throws Exception {

		List<Customer> customers = Arrays.asList(
				createCustomer(1, "Julia", "Mendez", "11111111", "jmendez@mail.com"),
				createCustomer(2, "Alicia", "Ramirez", "22222222", "aramirez@mail.com"),
				createCustomer(3, "Jose", "Melendez", "33333333", "jmelendez@mail.com")
		);

		when(customerService.getAllActiveCustomers()).thenReturn(customers);

		mockMvc.perform(get("/api/v1/customers")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(3))
				.andExpect(jsonPath("$[0].documentNumber").value("11111111"))
				.andExpect(jsonPath("$[0].email").value("jmendez@mail.com"))
				.andExpect(jsonPath("$[1].firstName").value("Alicia"))
				.andExpect(jsonPath("$[2].lastName").value("Melendez"))
				.andDo(print());

		verify(customerService).getAllActiveCustomers();
	}

	@Test
	@DisplayName("GET /api/v1/customers - returns empty list when no active customers exist")
	void getAllCustomers_WithNoActiveCustomers_ReturnsEmptyList() throws Exception {

		when(customerService.getAllActiveCustomers()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/v1/customers")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(0))
				.andDo(print());

		verify(customerService).getAllActiveCustomers();
	}

	@Test
	@DisplayName("GET /api/v1/customers/{customerId} - returns customer when found")
	void getCustomerById_WithValidId_ReturnsCustomerDTO() throws Exception {

		Integer customerId = 1;
		Customer customer = createCustomer(customerId, "Julia", "Mendez",
				"11111111", "jmendez@mail.com");
		when(customerService.getCustomerById(customerId)).thenReturn(customer);

		mockMvc.perform(get("/api/v1/customers/{customerId}", customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.customerId").value(customerId))
				.andExpect(jsonPath("$.firstName").value("Julia"))
				.andExpect(jsonPath("$.documentNumber").value("11111111"))
				.andExpect(jsonPath("$.email").value("jmendez@mail.com"))
				.andDo(print());

		verify(customerService).getCustomerById(customerId);
	}

	@Test
	@DisplayName("GET /api/v1/customers/{customerId} - returns 404 when customer not found")
	void getCustomerById_WithNonExistentId_ReturnsNotFound() throws Exception {

		Integer nonExistentId = 999;
		when(customerService.getCustomerById(nonExistentId))
				.thenThrow(new CustomerNotFoundException("Customer not found"));

		mockMvc.perform(get("/api/v1/customers/{customerId}", nonExistentId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Customer not found"))
				.andDo(print());

		verify(customerService).getCustomerById(nonExistentId);
	}

	@Test
	@DisplayName("POST /api/v1/customers - creates customer successfully")
	void createCustomer_WithValidData_ReturnsCreatedCustomer() throws Exception {

		CustomerDTO requestDTO = createCustomerDTO(null, "Maria", "Luna",
				"44444444", "mluna@mail.com");
		Customer savedCustomer = createCustomer(4, "Maria", "Luna",
				"44444444", "mluna@mail.com");

		when(customerService.createCustomer(any(Customer.class))).thenReturn(savedCustomer);

		mockMvc.perform(post("/api/v1/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(requestDTO))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.customerId").value(4))
				.andExpect(jsonPath("$.firstName").value("Maria"))
				.andExpect(jsonPath("$.lastName").value("Luna"))
				.andExpect(jsonPath("$.documentNumber").value("44444444"))
				.andExpect(jsonPath("$.email").value("mluna@mail.com"))
				.andDo(print());

		verify(customerService).createCustomer(any(Customer.class));
	}

	@Test
	@DisplayName("POST /api/v1/customers - returns 400 when document number already exists")
	void createCustomer_WithDuplicateDocumentNumber_ReturnsBadRequest() throws Exception {

		CustomerDTO duplicateDTO = createCustomerDTO(null, "Julia", "Mendez",
				"11111111", "jmendez@mail.com");

		when(customerService.createCustomer(any(Customer.class)))
				.thenThrow(new CustomerValidationException(
						"Customer with document number 11111111 already exists"));

		mockMvc.perform(post("/api/v1/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(duplicateDTO))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Customer with document number 11111111 already exists"))
				.andDo(print());

		verify(customerService).createCustomer(any(Customer.class));
	}

	@ParameterizedTest
	@MethodSource("invalidCustomerDTOs")
	@DisplayName("POST /api/v1/customers - returns 400 for invalid input data")
	void createCustomer_WithInvalidData_ReturnsBadRequest(CustomerDTO invalidDTO)
			throws Exception {

		mockMvc.perform(post("/api/v1/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(invalidDTO))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andDo(print());

		verifyNoInteractions(customerService);
	}

	static Stream<CustomerDTO> invalidCustomerDTOs() {
		return Stream.of(
				// Blank firstName
				new CustomerDTO(null, "", "Lastname", "11111111", "email@test.com",
						"963852741", "Lima, Peru"),
				// Blank lastName
				new CustomerDTO(null, "Name", "", "11111111", "email@test.com",
						"963852741", "Lima, Peru"),
				// Blank documentNumber
				new CustomerDTO(null, "Name", "Last", "", "email@test.com",
						"963852741", "Lima, Peru"),
				// Invalid email format
				new CustomerDTO(null, "Name", "Last", "11111111", "invalid-email",
						"963852741", "Lima, Peru"),
				// Null firstName
				new CustomerDTO(null, null, "Last", "11111111", "email@test.com",
						"963852741", "Lima, Peru")
		);
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/update/{customerId} - updates customer successfully")
	void updateCustomer_WithValidData_ReturnsUpdatedCustomer() throws Exception {

		Integer customerId = 2;
		UpdateCustomerDTO updateDTO = new UpdateCustomerDTO(
				"updated@mail.com", "987654321", "Arequipa, Peru");

		Customer updatedCustomer = createCustomer(customerId, "Jose", "Melendez",
				"33333333", "updated@mail.com");
		updatedCustomer.setPhoneNumber("987654321");
		updatedCustomer.setAddress("Arequipa, Peru");

		when(customerService.updateCustomerById(eq(customerId), any(Customer.class)))
				.thenReturn(updatedCustomer);

		mockMvc.perform(patch("/api/v1/customers/update/{customerId}", customerId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(updateDTO))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.customerId").value(customerId))
				.andExpect(jsonPath("$.email").value("updated@mail.com"))
				.andExpect(jsonPath("$.phoneNumber").value("987654321"))
				.andExpect(jsonPath("$.address").value("Arequipa, Peru"))
				.andDo(print());

		verify(customerService).updateCustomerById(eq(customerId), any(Customer.class));
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/update/{customerId} - returns 404 when customer not " +
			"found")
	void updateCustomer_WithNonExistentId_ReturnsNotFound() throws Exception {

		Integer nonExistentId = 999;
		UpdateCustomerDTO updateDTO = new UpdateCustomerDTO(
				"new@mail.com", "987654321", "New Address");

		when(customerService.updateCustomerById(eq(nonExistentId), any(Customer.class)))
				.thenThrow(new CustomerNotFoundException("Customer not found"));

		mockMvc.perform(patch("/api/v1/customers/update/{customerId}", nonExistentId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(toJson(updateDTO))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Customer not found"))
				.andDo(print());

		verify(customerService).updateCustomerById(eq(nonExistentId), any(Customer.class));
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/activate/{customerId} - activates customer successfully")
	void activateCustomer_WithValidId_ReturnsActivatedCustomer() throws Exception {

		Integer customerId = 1;
		Customer activatedCustomer = createCustomer(customerId, "Julia", "Mendez",
				"11111111", "jmendez@mail.com");

		when(customerService.activateCustomerById(customerId)).thenReturn(activatedCustomer);

		mockMvc.perform(patch("/api/v1/customers/activate/{customerId}", customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.customerId").value(customerId))
				.andDo(print());

		verify(customerService).activateCustomerById(customerId);
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/activate/{customerId} - returns 404 when customer not " +
			"found")
	void activateCustomer_WithNonExistentId_ReturnsNotFound() throws Exception {

		Integer nonExistentId = 999;

		when(customerService.activateCustomerById(nonExistentId))
				.thenThrow(new CustomerNotFoundException("Customer not found"));

		mockMvc.perform(patch("/api/v1/customers/activate/{customerId}", nonExistentId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Customer not found"))
				.andDo(print());

		verify(customerService).activateCustomerById(nonExistentId);
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/deactivate/{customerId} - deactivates customer " +
			"successfully")
	void deactivateCustomer_WithValidId_ReturnsDeactivatedCustomer() throws Exception {

		Integer customerId = 1;
		Customer deactivatedCustomer = createCustomer(customerId, "Julia", "Mendez",
				"11111111", "jmendez@mail.com");
		deactivatedCustomer.setActive(false);

		when(customerService.deactivateCustomerById(customerId)).thenReturn(deactivatedCustomer);

		mockMvc.perform(patch("/api/v1/customers/deactivate/{customerId}", customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.customerId").value(customerId))
				.andDo(print());

		verify(customerService).deactivateCustomerById(customerId);
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/deactivate/{customerId} - returns 404 when customer not " +
			"found")
	void deactivateCustomer_WithNonExistentId_ReturnsNotFound() throws Exception {

		Integer nonExistentId = 999;

		when(customerService.deactivateCustomerById(nonExistentId))
				.thenThrow(new CustomerNotFoundException("Customer not found"));

		mockMvc.perform(patch("/api/v1/customers/deactivate/{customerId}", nonExistentId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Customer not found"))
				.andDo(print());

		verify(customerService).deactivateCustomerById(nonExistentId);
	}

	@Test
	@DisplayName("PATCH /api/v1/customers/deactivate/{customerId} - returns 400 when customer has " +
			"active accounts")
	void deactivateCustomer_WithActiveAccounts_ReturnsBadRequest() throws Exception {

		Integer customerId = 1;

		when(customerService.deactivateCustomerById(customerId))
				.thenThrow(new CustomerValidationException(
						"Customer has accounts and cannot be deleted."));

		mockMvc.perform(patch("/api/v1/customers/deactivate/{customerId}", customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Customer has accounts and cannot be deleted."))
				.andDo(print());

		verify(customerService).deactivateCustomerById(customerId);
	}


	@Test
	@DisplayName("DELETE /api/v1/customers/{customerId} - deletes customer successfully")
	void deleteCustomer_WithValidId_ReturnsNoContent() throws Exception {

		Integer customerId = 1;
		doNothing().when(customerService).deleteCustomerById(customerId);

		mockMvc.perform(delete("/api/v1/customers/{customerId}", customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(print());

		verify(customerService).deleteCustomerById(customerId);
	}

	@Test
	@DisplayName("DELETE /api/v1/customers/{customerId} - returns 404 when customer not found")
	void deleteCustomer_WithNonExistentId_ReturnsNotFound() throws Exception {

		Integer nonExistentId = 999;

		doThrow(new CustomerNotFoundException("Customer not found"))
				.when(customerService).deleteCustomerById(nonExistentId);

		mockMvc.perform(delete("/api/v1/customers/{customerId}", nonExistentId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Customer not found"))
				.andDo(print());

		verify(customerService).deleteCustomerById(nonExistentId);
	}

	@Test
	@DisplayName("DELETE /api/v1/customers/{customerId} - returns 400 when customer has active " +
			"accounts")
	void deleteCustomer_WithActiveAccounts_ReturnsBadRequest() throws Exception {

		Integer customerId = 1;

		doThrow(new CustomerValidationException("Customer has accounts and cannot be deleted."))
				.when(customerService).deleteCustomerById(customerId);

		mockMvc.perform(delete("/api/v1/customers/{customerId}", customerId)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("Customer has accounts and cannot be deleted."))
				.andDo(print());

		verify(customerService).deleteCustomerById(customerId);
	}

	private Customer createCustomer(Integer id, String firstName, String lastName,
									String documentNumber, String email) {
		return Customer.builder()
				.customerId(id)
				.firstName(firstName)
				.lastName(lastName)
				.documentNumber(documentNumber)
				.email(email)
				.phoneNumber("963852741")
				.address("Lima, Peru")
				.creationDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.active(true)
				.build();
	}

	private CustomerDTO createCustomerDTO(Integer id, String firstName, String lastName,
										  String documentNumber, String email) {

		return new CustomerDTO(id, firstName, lastName, documentNumber, email,
				"963852741", "Lima, Peru");
	}

	private String toJson(Object obj) throws Exception {
		return objectMapper.writeValueAsString(obj);
	}
}

