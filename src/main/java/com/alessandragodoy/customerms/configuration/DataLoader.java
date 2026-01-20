package com.alessandragodoy.customerms.configuration;

import com.alessandragodoy.customerms.model.Customer;
import com.alessandragodoy.customerms.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataLoader is a component that initializes the database with initial customer data
 * if no customers are found in the repository.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);
	private final CustomerRepository customerRepository;

	@Override
	public void run(String... args) {
		if (customerRepository.count() == 0) {
			LOGGER.info("No customers found, creating initial customers...");
			List<Customer> initialCustomers =
					List.of(Customer.builder().firstName("Julia").lastName("Mendez")
									.documentNumber("11111111").email("jmendez@mail.com")
									.phoneNumber("963852741").address("Lima, Perú").build(),
							Customer.builder().firstName("Alicia").lastName("Ramirez")
									.documentNumber("22222222").email("aramirez@mail.com")
									.phoneNumber("741852963").address("Lima, Perú").build(),
							Customer.builder().firstName("Jose").lastName("Melendez")
									.documentNumber("33333333").email("jmelendez@mail.com")
									.phoneNumber("863214569").address("Lima, Perú").build(),
							Customer.builder().firstName("Carla").lastName("Gomez")
									.documentNumber("44444444").email("cgomez@mail.com")
									.phoneNumber("963963852").address("Lima, " + "Perú").build(),
							Customer.builder().firstName("Juan").lastName("Perez")
									.documentNumber("55555555").email("jperez@mail.com")
									.phoneNumber("741874521").address("Lima, " + "Perú").build());

			customerRepository.saveAll(initialCustomers);
			LOGGER.info("Initial customers added to the database.");
		} else {
			LOGGER.info("Customers already exist, skipping initialization.");
		}
	}
}
