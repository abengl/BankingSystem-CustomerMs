package com.alessandragodoy.customerms.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for OpenAPI documentation.
 * This class sets up the OpenAPI definition for the Customer Microservice.
 */
@Configuration
public class OpenApiConfig {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${server.port}")
	private String serverPort;

	@Bean
	public OpenAPI customOpenAPI() {
		List<Server> servers = new ArrayList<>();

		if ("dev".equals(activeProfile)) {
			servers.add(new Server()
					.url("http://localhost:" + serverPort)
					.description("Local development server"));
		}

		servers.add(new Server()
				.url("https://customers.alessandragodoy.com")
				.description("Production server"));

		return new OpenAPI()
				.info(new Info()
						.title("Banking System - Customer Microservice API")
						.version("1.2.0")
						.description("""
								Client-facing API for managing customers with full CRUD operations.
								This API provides endpoints for:
								- Creating new customers
								- Retrieving customer information
								- Updating customer details
								- Activating/deactivating customers
								""")
						.contact(new Contact().name("Alessandra Godoy")
								.email("api@alessandragodoy.com")))
				.servers(servers);
	}
}
