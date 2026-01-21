package com.alessandragodoy.customerms.utility;

import com.alessandragodoy.customerms.controller.dto.CustomerDTO;
import com.alessandragodoy.customerms.model.Customer;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * Singleton bean for mapping between Customer and CustomerDTO.
 * Thread-safe because it is stateless.
 */
@NoArgsConstructor
public class DTOMapper {

	private static final ModelMapper MAPPER = new ModelMapper();

	public static CustomerDTO convertToDTO(Customer customer) {
		return MAPPER.map(customer, CustomerDTO.class);
	}

	public static Customer convertToEntity(CustomerDTO customerDTO) {
		return MAPPER.map(customerDTO, Customer.class);
	}

	public static Customer dtoCreateToEntity(CustomerDTO customerDTO) {
		return Customer.builder()
				.firstName(customerDTO.firstName())
				.lastName(customerDTO.lastName())
				.documentNumber(customerDTO.documentNumber())
				.email(customerDTO.email())
				.phoneNumber(customerDTO.phoneNumber())
				.address(customerDTO.address())
				.build();
	}
}
