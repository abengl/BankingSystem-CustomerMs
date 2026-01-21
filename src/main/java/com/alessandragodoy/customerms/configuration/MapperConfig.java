package com.alessandragodoy.customerms.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

	@Bean
	public ModelMapper defaultModelMapper() {
		return new ModelMapper();
	}
}
