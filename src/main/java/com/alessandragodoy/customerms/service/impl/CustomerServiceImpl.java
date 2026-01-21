package com.alessandragodoy.customerms.service.impl;

import com.alessandragodoy.customerms.adapter.CustomerAdapter;
import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.utility.CustomerMapper;
import com.alessandragodoy.customerms.exception.AccountsNotFoundException;
import com.alessandragodoy.customerms.exception.CustomerValidationException;
import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.repository.CustomerRepository;
import com.alessandragodoy.customerms.service.CustomerService;
import com.alessandragodoy.customerms.utility.CustomerValidationUtils;
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
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerAdapter customerAdapter;

	/* Customer MS CRUD methods */
	@Override
	public List<CustomerDTO> getAllCustomers() {
		return customerRepository.findAll().stream().map(CustomerMapper::toDTO).toList();
	}

	@Override
	public CustomerDTO getCustomerById(Integer customerId) {
		return customerRepository.findById(customerId).map(CustomerMapper::toDTO)
				.orElseThrow(() -> new AccountsNotFoundException("Customer not found"));
	}

	@Override
	public CustomerDTO updateCustomerById(Integer customerId, CustomerDTO customerDTO) {
		validateCustomerData(customerDTO);
		return customerRepository.findById(customerId).map(existingCustomer -> {
			Customer updatedCustomer = CustomerMapper.toEntity(customerDTO);
			customerRepository.save(updatedCustomer);
			return CustomerMapper.toDTO(updatedCustomer);
		}).orElseThrow(() -> new AccountsNotFoundException("Customer not found"));
	}

	@Override
	public CustomerDTO createCustomer(CustomerDTO customerDTO) {
		validateCustomerData(customerDTO);
		Customer customer = CustomerMapper.dtoCreateToEntity(customerDTO);
		customerRepository.save(customer);
		return CustomerMapper.toDTO(customer);
	}

	@Override
	public CustomerDTO deleteCustomerById(Integer customerId) {
		if (customerAdapter.customerHasAccounts(customerId)) {
			throw new CustomerValidationException("Customer has accounts and cannot be deleted.");
		}
		return customerRepository.findById(customerId).map(existingCustomer -> {
			customerRepository.delete(existingCustomer);
			return CustomerMapper.toDTO(existingCustomer);
		}).orElseThrow(() -> new AccountsNotFoundException("Customer not found"));
	}

	@Override
	public boolean customerExists(Integer customerId) {
		return customerRepository.existsById(customerId);
	}

	/* Helper methods */
	private void validateCustomerData(CustomerDTO customerDTO) {
		CustomerValidationUtils.checkRequiredFields(
				customerDTO.firstName(),
				customerDTO.lastName(),
				customerDTO.dni(),
				customerDTO.email()
		);
		CustomerValidationUtils.checkDniFormat(customerDTO.dni());
		CustomerValidationUtils.checkEmailFormat(customerDTO.email());
		checkDniUniqueness(customerDTO.dni(), customerDTO.customerId());

	}

	private void checkDniUniqueness(String dni, Integer customerId) {
		Optional<Customer> customerOptional = customerRepository.findByDocumentNumber(dni);
		customerOptional.ifPresent(customer -> {
			if (!customer.getCustomerId().equals(customerId)) {
				throw new CustomerValidationException("DNI number is already registered.");
			}
		});
	}
}

