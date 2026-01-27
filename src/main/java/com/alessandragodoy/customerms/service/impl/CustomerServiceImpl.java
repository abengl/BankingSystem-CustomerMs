package com.alessandragodoy.customerms.service.impl;

import com.alessandragodoy.customerms.adapter.AccountServiceClient;
import com.alessandragodoy.customerms.controller.dto.CustomerValidationResponseDTO;
import com.alessandragodoy.customerms.exception.CustomerNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.repository.CustomerRepository;
import com.alessandragodoy.customerms.service.ICustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the CustomerService interface.
 * Provides methods for managing customers.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

	private final CustomerRepository customerRepository;
	private final AccountServiceClient accountServiceClient;

	@Override
	public List<Customer> getAllActiveCustomers() {

		return customerRepository.findAllByActiveTrue();
	}

	@Override
	public Customer getCustomerById(Integer customerId) {

		return customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
	}

	@Override
	public Customer createCustomer(Customer customer) {

		if (customerRepository.existsByDocumentNumber(customer.getDocumentNumber())) {
			throw new CustomerValidationException(
					"Customer with document number " + customer.getDocumentNumber() + " already exists");
		}
		return customerRepository.save(customer);

	}

	@Transactional
	@Override
	public Customer updateCustomerById(Integer customerId, Customer customer) {

		Customer updatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

		if (!customer.getEmail().isBlank()) {
			updatedCustomer.setEmail(customer.getEmail());
		}
		if (!customer.getPhoneNumber().isBlank()) {
			updatedCustomer.setPhoneNumber(customer.getPhoneNumber());
		}
		if (!customer.getAddress().isBlank()) {
			updatedCustomer.setAddress(customer.getAddress());
		}

		return customerRepository.save(updatedCustomer);
	}

	@Transactional
	@Override
	public Customer activateCustomerById(Integer customerId) {

		Customer activatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

		activatedCustomer.setActive(true);

		return customerRepository.save(activatedCustomer);
	}

	@Transactional
	@Override
	public Customer deactivateCustomerById(Integer customerId) {

		Customer deactivatedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

		if (accountServiceClient.customerHasActiveAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}

		deactivatedCustomer.setActive(false);

		return customerRepository.save(deactivatedCustomer);
	}

	@Transactional
	@Override
	public void deleteCustomerById(Integer customerId) {

		Customer deletedCustomer =
				customerRepository.findById(customerId)
						.orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

		if (accountServiceClient.customerHasActiveAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}

		customerRepository.delete(deletedCustomer);
	}

	@Override
	public CustomerValidationResponseDTO validateCustomer(Integer customerId) {

		Optional<Customer> customerOptional = customerRepository.findById(customerId);

		if (customerOptional.isEmpty()) {
			return CustomerValidationResponseDTO.invalid(
					"Customer not found for ID: " + customerId);
		}

		Customer customer = customerOptional.get();
		if (!customer.isActive()) {
			return CustomerValidationResponseDTO.invalid(
					"Customer is not active for ID: " + customerId);
		}

		return CustomerValidationResponseDTO.valid();
	}

}

