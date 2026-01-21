package com.alessandragodoy.customerms;

import com.alessandragodoy.customerms.adapter.CustomerAdapter;
import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.repository.CustomerRepository;
import com.alessandragodoy.customerms.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceValidTests {
	public List<Customer> customers = new ArrayList<>();
	@Mock
	CustomerAdapter customerAdapter;
	@InjectMocks
	private CustomerServiceImpl customerService;
	@Mock
	private CustomerRepository customerRepository;

	@BeforeEach
	public void setUp() {
		customers.add(Customer.builder()
				.customerId(1).firstName("Jose").lastName("Gomez")
				.documentNumber("12345678").email("jose.gomez@mail.com").phoneNumber("963852741").address("Lima, Perú").build());
		customers.add(Customer.builder()
				.customerId(2).firstName("Maria").lastName("Perez")
				.documentNumber("87654321").email("maria.perze@mail.com").phoneNumber("963852741").address("Lima, Perú").build());
		customers.add(Customer.builder()
				.customerId(3).firstName("Juan").lastName("Lopez")
				.documentNumber("45678912").email("juan.lopez@mail.com").phoneNumber("963852741").address("Lima, Perú").build()
		);
	}

	@Test
	@DisplayName("Test customerExists by id method - it returns a boolean")
	void CustomerService_CustomerExists_ReturnsBoolean() {
		// Arrange
		int customerId = 1;
		when(customerRepository.existsById(customerId)).thenReturn(true);

		// Act
		boolean result1 = customerService.customerExists(customerId);

		// Assert
		assertTrue(result1);
	}

	@Test
	@DisplayName("Test getAllCustomers method - it returns CustomerDTO list")
	void CustomerService_GetAllCustomers_ReturnsCustomerDTOList() {
		// Arrange
		when(customerRepository.findAll()).thenReturn(customers);

		// Act
		List<CustomerDTO> customersDTO = customerService.getAllCustomers();

		// Assert
		assertEquals(3, customersDTO.size());
		assertEquals("Jose", customersDTO.get(0).firstName());
	}

	@Test
	@DisplayName("Test getCustomerById method - it returns CustomerDTO")
	void CustomerService_GetCustomerById_ReturnsCustomerDTO() {
		// Arrange
		int customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(customers.get(0)));

		// Act
		CustomerDTO customerDTO = customerService.getCustomerById(customerId);

		// Assert
		assertNotNull(customerDTO);
		assertEquals("12345678", customerDTO.dni());
	}

	@Test
	@DisplayName("Test updateCustomerById method - it returns CustomerDTO")
	void CustomerService_UpdateCustomerById_ReturnsCustomerDTO() {
		// Arrange
		int customerId = 1;
		CustomerDTO customerDTORequest =
				new CustomerDTO(customerId, "Jose", "Gomez", "88888888", "jose.gomez@mail.com");
		Customer existingCustomer = customers.get(0);
		Customer customerUpdated = Customer.builder()
				.firstName("Jose").lastName("Gomez")
				.documentNumber("88888888").email("jose.gomez@mail.com").build();
		when(customerRepository.findByDocumentNumber(customerDTORequest.dni())).thenReturn(Optional.empty());
		when(customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenReturn(customerUpdated);

		// Act
		CustomerDTO customerDTO = customerService.updateCustomerById(customerId, customerDTORequest);

		// Assert
		assertNotNull(customerDTO);
		assertEquals("88888888", customerDTO.dni());
		verify(customerRepository, times(1)).findByDocumentNumber(customerDTORequest.dni());
		verify(customerRepository, times(1)).findById(customerId);
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	@Test
	@DisplayName("Test createCustomer method - it returns CustomerDTO")
	void CustomerService_CreateCustomer_ReturnsCustomerDTO() {
		// Arrange
		CustomerDTO customerDTORequest =
				new CustomerDTO(4, "Ale", "Luna", "96385296", "ale.luna@mail.com");
		Customer newCustomer =
				Customer.builder().customerId(4).firstName("Ale").lastName("Luna").documentNumber(
						"96385296").email("ale" +
						".luna@mail.com").build();
		when(customerRepository.findByDocumentNumber(customerDTORequest.dni())).thenReturn(Optional.empty());
		when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

		// Act
		CustomerDTO customerDTO = customerService.createCustomer(customerDTORequest);

		// Assert
		assertNotNull(customerDTO);
		assertEquals("96385296", customerDTO.dni());
		verify(customerRepository, times(1)).findByDocumentNumber(customerDTORequest.dni());
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	@Test
	@DisplayName("Test deleteCustomerById method - it returns CustomerDTO")
	void CustomerService_DeleteCustomerById_ReturnsCustomerDTO() {
		// Arrange
		int customerId = 2;
		Customer existingCustomer = customers.get(customerId);

		when(customerAdapter.customerHasAccounts(customerId)).thenReturn(false);
		when(customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(existingCustomer));
		doNothing().when(customerRepository).delete(existingCustomer);

		// Act
		CustomerDTO customerDTO = customerService.deleteCustomerById(customerId);


		// Assert
		assertNotNull(customerDTO);
		assertEquals("45678912", customerDTO.dni());
		verify(customerRepository, times(1)).findById(customerId);
		verify(customerRepository, times(1)).delete(existingCustomer);
	}

}
