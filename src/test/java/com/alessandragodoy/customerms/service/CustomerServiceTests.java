package com.alessandragodoy.customerms.service;

import com.alessandragodoy.customerms.adapter.AccountServiceClient;
import com.alessandragodoy.customerms.dto.CustomerValidationResponseDTO;
import com.alessandragodoy.customerms.exception.CustomerNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ICustomerService}
 * Tests business logic and interaction with repository and external services.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AccountServiceClient accountServiceClient;

	@InjectMocks
	private CustomerServiceImpl customerService;

	private Customer testCustomer;
	private Customer testCustomer2;
	private Customer inactiveCustomer;

	@BeforeEach
	void setUp() {
		testCustomer = createCustomer(1, "Julia", "Mendez", "11111111",
				"jmendez@mail.com", true);
		testCustomer2 = createCustomer(2, "Alicia", "Ramirez", "22222222",
				"aramirez@mail.com", true);
		inactiveCustomer = createCustomer(3, "Jose", "Melendez", "33333333",
				"jmelendez@mail.com", false);
	}

	@Test
	@DisplayName("getAllActiveCustomers - returns list of active customers")
	void getAllActiveCustomers_WithActiveCustomers_ReturnsCustomerList() {

		List<Customer> activeCustomers = Arrays.asList(testCustomer, testCustomer2);
		when(customerRepository.findAllByActiveTrue()).thenReturn(activeCustomers);

		List<Customer> result = customerService.getAllActiveCustomers();

		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(testCustomer, testCustomer2);
		assertThat(result).allMatch(Customer::isActive);

		verify(customerRepository).findAllByActiveTrue();
	}

	@Test
	@DisplayName("getAllActiveCustomers - returns empty list when no active customers")
	void getAllActiveCustomers_WithNoActiveCustomers_ReturnsEmptyList() {

		when(customerRepository.findAllByActiveTrue()).thenReturn(Collections.emptyList());

		List<Customer> result = customerService.getAllActiveCustomers();

		assertThat(result).isNotNull();
		assertThat(result).isEmpty();

		verify(customerRepository).findAllByActiveTrue();
	}

	@Test
	@DisplayName("getCustomerById - returns customer when found")
	void getCustomerById_WithExistingId_ReturnsCustomer() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));

		Customer result = customerService.getCustomerById(customerId);

		assertThat(result).isNotNull();
		assertThat(result.getCustomerId()).isEqualTo(customerId);
		assertThat(result.getFirstName()).isEqualTo("Julia");
		assertThat(result.getDocumentNumber()).isEqualTo("11111111");

		verify(customerRepository).findById(customerId);
	}

	@Test
	@DisplayName("getCustomerById - throws CustomerNotFoundException when not found")
	void getCustomerById_WithNonExistentId_ThrowsCustomerNotFoundException() {

		Integer nonExistentId = 999;
		when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.getCustomerById(nonExistentId))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found");

		verify(customerRepository).findById(nonExistentId);
	}

	@Test
	@DisplayName("createCustomer - creates customer successfully")
	void createCustomer_WithValidData_ReturnsCreatedCustomer() {

		Customer newCustomer = createCustomer(null, "Maria", "Luna", "44444444",
				"mluna@mail.com", true);
		Customer savedCustomer = createCustomer(4, "Maria", "Luna", "44444444",
				"mluna@mail.com", true);

		when(customerRepository.existsByDocumentNumber("44444444")).thenReturn(false);
		when(customerRepository.save(newCustomer)).thenReturn(savedCustomer);

		Customer result = customerService.createCustomer(newCustomer);

		assertThat(result).isNotNull();
		assertThat(result.getCustomerId()).isEqualTo(4);
		assertThat(result.getDocumentNumber()).isEqualTo("44444444");
		assertThat(result.getFirstName()).isEqualTo("Maria");

		verify(customerRepository).existsByDocumentNumber("44444444");
		verify(customerRepository).save(newCustomer);
	}

	@Test
	@DisplayName("createCustomer - throws exception when document number already exists")
	void createCustomer_WithDuplicateDocumentNumber_ThrowsCustomerValidationException() {

		Customer duplicateCustomer = createCustomer(null, "Test", "User", "11111111",
				"test@mail.com", true);

		when(customerRepository.existsByDocumentNumber("11111111")).thenReturn(true);

		assertThatThrownBy(() -> customerService.createCustomer(duplicateCustomer))
				.isInstanceOf(CustomerValidationException.class)
				.hasMessage("Customer with document number 11111111 already exists");

		verify(customerRepository).existsByDocumentNumber("11111111");
		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	@DisplayName("updateCustomerById - updates all fields successfully")
	void updateCustomerById_WithAllFields_ReturnsUpdatedCustomer() {

		Integer customerId = 1;
		Customer updateData = Customer.builder()
				.email("newemail@mail.com")
				.phoneNumber("987654321")
				.address("Arequipa, Peru")
				.build();

		Customer existingCustomer = createCustomer(1, "Julia", "Mendez", "11111111",
				"jmendez@mail.com", true);

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.updateCustomerById(customerId, updateData);

		assertThat(result).isNotNull();
		assertThat(result.getEmail()).isEqualTo("newemail@mail.com");
		assertThat(result.getPhoneNumber()).isEqualTo("987654321");
		assertThat(result.getAddress()).isEqualTo("Arequipa, Peru");
		assertThat(result.getFirstName()).isEqualTo("Julia"); // Unchanged
		assertThat(result.getDocumentNumber()).isEqualTo("11111111"); // Unchanged

		verify(customerRepository).findById(customerId);
		verify(customerRepository).save(existingCustomer);
	}

	@Test
	@DisplayName("updateCustomerById - updates only email when other fields are blank")
	void updateCustomerById_WithOnlyEmail_UpdatesEmailOnly() {

		Integer customerId = 1;
		Customer updateData = Customer.builder()
				.email("newemail@mail.com")
				.phoneNumber("")
				.address("")
				.build();

		Customer existingCustomer = createCustomer(1, "Julia", "Mendez", "11111111",
				"jmendez@mail.com", true);
		String originalPhone = existingCustomer.getPhoneNumber();
		String originalAddress = existingCustomer.getAddress();

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.updateCustomerById(customerId, updateData);

		assertThat(result.getEmail()).isEqualTo("newemail@mail.com");
		assertThat(result.getPhoneNumber()).isEqualTo(originalPhone); // Unchanged
		assertThat(result.getAddress()).isEqualTo(originalAddress); // Unchanged

		verify(customerRepository).findById(customerId);
		verify(customerRepository).save(existingCustomer);
	}

	@Test
	@DisplayName("updateCustomerById - throws exception when customer not found")
	void updateCustomerById_WithNonExistentId_ThrowsCustomerNotFoundException() {

		Integer nonExistentId = 999;
		Customer updateData = Customer.builder()
				.email("test@mail.com")
				.phoneNumber("123456789")
				.address("Test Address")
				.build();

		when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.updateCustomerById(nonExistentId, updateData))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found");

		verify(customerRepository).findById(nonExistentId);
		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	@DisplayName("updateCustomerById - does not update when all fields are blank")
	void updateCustomerById_WithAllBlankFields_KeepsOriginalData() {

		Integer customerId = 1;
		Customer updateData = Customer.builder()
				.email("")
				.phoneNumber("")
				.address("")
				.build();

		Customer existingCustomer = createCustomer(1, "Julia", "Mendez", "11111111",
				"jmendez@mail.com", true);
		String originalEmail = existingCustomer.getEmail();
		String originalPhone = existingCustomer.getPhoneNumber();
		String originalAddress = existingCustomer.getAddress();

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.updateCustomerById(customerId, updateData);

		assertThat(result.getEmail()).isEqualTo(originalEmail);
		assertThat(result.getPhoneNumber()).isEqualTo(originalPhone);
		assertThat(result.getAddress()).isEqualTo(originalAddress);

		verify(customerRepository).save(existingCustomer);
	}

	@Test
	@DisplayName("activateCustomerById - activates inactive customer successfully")
	void activateCustomerById_WithInactiveCustomer_ReturnsActivatedCustomer() {

		Integer customerId = 3;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(inactiveCustomer));
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.activateCustomerById(customerId);

		assertThat(result).isNotNull();
		assertThat(result.isActive()).isTrue();
		assertThat(result.getCustomerId()).isEqualTo(customerId);

		verify(customerRepository).findById(customerId);
		verify(customerRepository).save(inactiveCustomer);
	}

	@Test
	@DisplayName("activateCustomerById - activates already active customer without error")
	void activateCustomerById_WithActiveCustomer_RemainsActive() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.activateCustomerById(customerId);

		assertThat(result.isActive()).isTrue();

		verify(customerRepository).findById(customerId);
		verify(customerRepository).save(testCustomer);
	}

	@Test
	@DisplayName("activateCustomerById - throws exception when customer not found")
	void activateCustomerById_WithNonExistentId_ThrowsCustomerNotFoundException() {

		Integer nonExistentId = 999;
		when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.activateCustomerById(nonExistentId))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found");

		verify(customerRepository).findById(nonExistentId);
		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	@DisplayName("deactivateCustomerById - deactivates customer without active accounts")
	void deactivateCustomerById_WithoutActiveAccounts_ReturnsDeactivatedCustomer() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
		when(accountServiceClient.customerHasActiveAccounts(customerId)).thenReturn(false);
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.deactivateCustomerById(customerId);

		assertThat(result).isNotNull();
		assertThat(result.isActive()).isFalse();
		assertThat(result.getCustomerId()).isEqualTo(customerId);

		verify(customerRepository).findById(customerId);
		verify(accountServiceClient).customerHasActiveAccounts(customerId);
		verify(customerRepository).save(testCustomer);
	}

	@Test
	@DisplayName("deactivateCustomerById - throws exception when customer has active accounts")
	void deactivateCustomerById_WithActiveAccounts_ThrowsCustomerValidationException() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
		when(accountServiceClient.customerHasActiveAccounts(customerId)).thenReturn(true);

		assertThatThrownBy(() -> customerService.deactivateCustomerById(customerId))
				.isInstanceOf(CustomerValidationException.class)
				.hasMessage("Customer has accounts and cannot be deleted.");

		verify(customerRepository).findById(customerId);
		verify(accountServiceClient).customerHasActiveAccounts(customerId);
		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	@DisplayName("deactivateCustomerById - throws exception when customer not found")
	void deactivateCustomerById_WithNonExistentId_ThrowsCustomerNotFoundException() {

		Integer nonExistentId = 999;
		when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.deactivateCustomerById(nonExistentId))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found");

		verify(customerRepository).findById(nonExistentId);
		verifyNoInteractions(accountServiceClient);
		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	@DisplayName("deactivateCustomerById - deactivates already inactive customer without error")
	void deactivateCustomerById_WithInactiveCustomer_RemainsInactive() {

		Integer customerId = 3;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(inactiveCustomer));
		when(accountServiceClient.customerHasActiveAccounts(customerId)).thenReturn(false);
		when(customerRepository.save(any(Customer.class))).thenAnswer(
				invocation -> invocation.getArgument(0));

		Customer result = customerService.deactivateCustomerById(customerId);

		assertThat(result.isActive()).isFalse();

		verify(accountServiceClient).customerHasActiveAccounts(customerId);
		verify(customerRepository).save(inactiveCustomer);
	}

	/*@Test
	@DisplayName("deleteCustomerById - deletes customer without active accounts")
	void deleteCustomerById_WithoutActiveAccounts_DeletesSuccessfully() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
		when(accountServiceClient.customerHasActiveAccounts(customerId)).thenReturn(false);
		doNothing().when(customerRepository).delete(testCustomer);

		customerService.deleteCustomerById(customerId);

		verify(customerRepository).findById(customerId);
		verify(accountServiceClient).customerHasActiveAccounts(customerId);
		verify(customerRepository).delete(testCustomer);
	}

	@Test
	@DisplayName("deleteCustomerById - throws exception when customer has active accounts")
	void deleteCustomerById_WithActiveAccounts_ThrowsCustomerValidationException() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));
		when(accountServiceClient.customerHasActiveAccounts(customerId)).thenReturn(true);

		assertThatThrownBy(() -> customerService.deleteCustomerById(customerId))
				.isInstanceOf(CustomerValidationException.class)
				.hasMessage("Customer has accounts and cannot be deleted.");

		verify(customerRepository).findById(customerId);
		verify(accountServiceClient).customerHasActiveAccounts(customerId);
		verify(customerRepository, never()).delete(any(Customer.class));
	}

	@Test
	@DisplayName("deleteCustomerById - throws exception when customer not found")
	void deleteCustomerById_WithNonExistentId_ThrowsCustomerNotFoundException() {

		Integer nonExistentId = 999;
		when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> customerService.deleteCustomerById(nonExistentId))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found");

		verify(customerRepository).findById(nonExistentId);
		verifyNoInteractions(accountServiceClient);
		verify(customerRepository, never()).delete(any(Customer.class));
	}
*/
	@Test
	@DisplayName("validateCustomer - returns valid response for active customer")
	void validateCustomer_WithActiveCustomer_ReturnsValidResponse() {

		Integer customerId = 1;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(testCustomer));

		CustomerValidationResponseDTO result = customerService.validateCustomer(customerId);

		assertThat(result).isNotNull();
		assertThat(result.getExists()).isTrue();
		assertThat(result.getIsActive()).isTrue();

		verify(customerRepository).findById(customerId);
	}

	@Test
	@DisplayName("validateCustomer - returns inactive response for inactive customer")
	void validateCustomer_WithInactiveCustomer_ReturnsInactiveResponse() {

		Integer customerId = 3;
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(inactiveCustomer));

		CustomerValidationResponseDTO result = customerService.validateCustomer(customerId);

		assertThat(result).isNotNull();
		assertThat(result.getExists()).isTrue();
		assertThat(result.getIsActive()).isFalse();
		assertThat(result.getMessage()).isEqualTo("Customer is not active for ID: " + customerId);

		verify(customerRepository).findById(customerId);
	}

	@Test
	@DisplayName("validateCustomer - returns invalid response when customer not found")
	void validateCustomer_WithNonExistentCustomer_ReturnsInvalidResponse() {

		Integer nonExistentId = 999;
		when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

		CustomerValidationResponseDTO result = customerService.validateCustomer(nonExistentId);

		assertThat(result).isNotNull();
		assertThat(result.getExists()).isFalse();
		assertThat(result.getMessage()).isEqualTo("Customer not found for ID: " + nonExistentId);

		verify(customerRepository).findById(nonExistentId);
	}

	@Test
	@DisplayName("Integration - create, update, deactivate, activate workflow")
	void integration_FullCustomerLifecycle_WorksCorrectly() {

		Customer newCustomer = createCustomer(null, "Test", "User", "99999999",
				"test@mail.com", true);
		Customer savedCustomer = createCustomer(10, "Test", "User", "99999999",
				"test@mail.com", true);

		when(customerRepository.existsByDocumentNumber("99999999")).thenReturn(false);
		when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
		when(customerRepository.findById(10)).thenReturn(Optional.of(savedCustomer));
		when(accountServiceClient.customerHasActiveAccounts(10)).thenReturn(false);

		// Create
		Customer created = customerService.createCustomer(newCustomer);
		assertThat(created.getCustomerId()).isEqualTo(10);

		// Update
		Customer updateData = Customer.builder()
				.email("updated@mail.com")
				.phoneNumber("111111111")
				.address("New Address")
				.build();
		Customer updated = customerService.updateCustomerById(10, updateData);
		assertThat(updated.getEmail()).isEqualTo("updated@mail.com");

		// Deactivate
		savedCustomer.setActive(false);
		Customer deactivated = customerService.deactivateCustomerById(10);
		assertThat(deactivated.isActive()).isFalse();

		// Activate
		when(customerRepository.findById(10)).thenReturn(Optional.of(savedCustomer));
		Customer activated = customerService.activateCustomerById(10);
		assertThat(activated.isActive()).isTrue();

		verify(customerRepository, atLeastOnce()).save(any(Customer.class));
		verify(accountServiceClient).customerHasActiveAccounts(10);
	}

	private Customer createCustomer(Integer id, String firstName, String lastName,
									String docNumber, String email, boolean active) {
		return Customer.builder()
				.customerId(id)
				.firstName(firstName)
				.lastName(lastName)
				.documentNumber(docNumber)
				.email(email)
				.phoneNumber("963852741")
				.address("Lima, Peru")
				.creationDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.active(active)
				.build();
	}

}