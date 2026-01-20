package com.alessandragodoy.customerms.utility;

import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.model.Customer;
import org.springframework.stereotype.Component;

/**
 * Singleton bean for mapping between Customer and CustomerDTO.
 * Thread-safe because it is stateless.
 */
@Component
public class CustomerMapper {
	public static CustomerDTO toDTO(Customer customer) {
		return new CustomerDTO(
				customer.getCustomerId(),
				customer.getFirstName(),
				customer.getLastName(),
				customer.getDocumentNumber(),
				customer.getEmail()
		);
	}

	public static Customer toEntity(CustomerDTO customerDTO) {
		return Customer.builder()
				.customerId(customerDTO.customerId())
				.firstName(customerDTO.firstName())
				.lastName(customerDTO.lastName())
				.documentNumber(customerDTO.dni())
				.email(customerDTO.email())
				.build();
	}

	public static Customer dtoCreateToEntity(CustomerDTO customerDTO) {
		return Customer.builder()
				.firstName(customerDTO.firstName())
				.lastName(customerDTO.lastName())
				.documentNumber(customerDTO.dni())
				.email(customerDTO.email())
				.build();
	}
}
