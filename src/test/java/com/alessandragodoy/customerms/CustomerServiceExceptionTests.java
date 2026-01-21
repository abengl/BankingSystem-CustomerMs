package com.alessandragodoy.customerms;

import com.alessandragodoy.customerms.adapter.CustomerAdapter;
import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.exception.AccountsNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.exception.ExternalServiceException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceExceptionTests {
	public List<Customer> customers = new ArrayList<>();

	@InjectMocks
	private CustomerServiceImpl customerService;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	CustomerAdapter customerAdapter;

	@BeforeEach
	public void setUp() {
		customers.add(Customer.builder()
				.customerId(1).firstName("Jose").lastName("Gomez")
				.documentNumber("12345678").email("jose.gomez@mail.com").build());
		customers.add(Customer.builder()
				.customerId(2).firstName("Maria").lastName("Perez")
				.documentNumber("87654321").email("maria.perze@mail.com").build());
		customers.add(Customer.builder()
				.customerId(3).firstName("Juan").lastName("Lopez")
				.documentNumber("45678912").email("juan.lopez@mail.com").build()
		);
	}

	@Test
	@DisplayName("Test getCustomerById method - it returns AccountsNotFoundException")
	void CustomerService_GetCustomerById_ReturnsAccountsNotFoundException() {
		// Arrange
		int customerId = 100;
		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		// Act & Assert
		AccountsNotFoundException exception = assertThrows(AccountsNotFoundException.class,
				() -> customerService.getCustomerById(customerId));
		assertEquals("Customer not found", exception.getMessage());
	}

	@Test
	@DisplayName("Test updateCustomerById method - it returns AccountsNotFoundException")
	void CustomerService_UpdateCustomerById_ReturnsAccountsNotFoundException() {
		// Arrange
		int customerId = 100;
		CustomerDTO customerDTORequest =
				new CustomerDTO(customerId, "Ana", "Lopez", "00000000", "alopez@mail.com");
		when(customerRepository.findByDocumentNumber(customerDTORequest.dni())).thenReturn(Optional.empty());
		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		// Act & Assert
		AccountsNotFoundException exception = assertThrows(AccountsNotFoundException.class,
				() -> customerService.updateCustomerById(customerId, customerDTORequest));
		assertEquals("Customer not found", exception.getMessage());
	}

	@Test
	@DisplayName("Test createCustomer method - it returns CustomerValidationException when request is missing values")
	void CustomerService_CreateCustomer_ReturnsCustomerValidationException() {
		// Arrange
		CustomerDTO customerDTORequest =
				new CustomerDTO(null, null, "", "", "");

		// Act & Assert
		CustomerValidationException exception = assertThrows(CustomerValidationException.class,
				() -> customerService.createCustomer(customerDTORequest));
		assertEquals("First name, last name, DNI, and email are required.", exception.getMessage());
	}

	@Test
	@DisplayName("Test createCustomer method - it returns CustomerValidationException when DNI is invalid")
	void CustomerService_CreateCustomer_ReturnsExceptionDNIInvalid() {
		// Arrange
		CustomerDTO customerDTORequest =
				new CustomerDTO(4, "Ale", "Luna", "ABC123", "ale.luna@mail.com");

		// Act & Assert
		CustomerValidationException exception = assertThrows(CustomerValidationException.class,
				() -> customerService.createCustomer(customerDTORequest));
		assertEquals("Invalid DNI format. It must contain exactly 8 digits.", exception.getMessage());
	}

	@Test
	@DisplayName("Test createCustomer method - it returns CustomerValidationException when email is invalid")
	void CustomerService_CreateCustomer_ReturnsExceptionEmailInvalid() {
		// Arrange
		CustomerDTO customerDTORequest =
				new CustomerDTO(4, "Ale", "Luna", "87654321", "@mail.com");

		// Act & Assert
		CustomerValidationException exception = assertThrows(CustomerValidationException.class,
				() -> customerService.createCustomer(customerDTORequest));
		assertEquals("Invalid email format. Format example 'user123@mail.com'", exception.getMessage());
	}

	@Test
	@DisplayName("Test createCustomer method - it returns CustomerValidationException when DNI is registered")
	void CustomerService_CreateCustomer_ReturnsExceptionDNIRegistered() {
		// Arrange
		CustomerDTO customerDTORequest =
				new CustomerDTO(4, "Ale", "Luna", "12345678", "aluna@email.com");
		when(customerRepository.findByDocumentNumber(customerDTORequest.dni())).thenReturn(Optional.ofNullable(customers.get(0)));
		// Act & Assert
		CustomerValidationException exception = assertThrows(CustomerValidationException.class,
				() -> customerService.createCustomer(customerDTORequest));
		assertEquals("DNI number is already registered.", exception.getMessage());
	}

	@Test
	@DisplayName("Test deleteCustomerById method - it returns CustomerValidationException")
	void CustomerService_DeleteCustomerById_ReturnsCustomerValidationException() {
		// Arrange
		int customerId = 2;

		when(customerAdapter.customerHasAccounts(customerId)).thenReturn(true);

		// Act & Assert
		CustomerValidationException exception = assertThrows(CustomerValidationException.class,
				() -> customerService.deleteCustomerById(customerId));
		assertEquals("Customer has accounts and cannot be deleted.", exception.getMessage());
	}

	@Test
	@DisplayName("Test deleteCustomerById method - it returns ExternalServiceException")
	void CustomerService_DeleteCustomerById_ReturnsExternalServiceException() {
		// Arrange
		int customerId = 2;

		doThrow(new ExternalServiceException("Unable to connect to the account service.")).when(customerAdapter).customerHasAccounts(customerId);

		// Act & Assert
		ExternalServiceException exception = assertThrows(ExternalServiceException.class,
				() -> customerService.deleteCustomerById(customerId));
		assertEquals("Unable to connect to the account service.", exception.getMessage());
	}
}
