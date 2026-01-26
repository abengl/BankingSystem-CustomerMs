package com.alessandragodoy.customerms.utility;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

/**
 * Singleton bean for mapping between Customer and CustomerDTO.
 * Thread-safe because it is stateless.
 */
@NoArgsConstructor
public class DTOMapper {

	private static final ModelMapper MAPPER = new ModelMapper();

	public static <D, E> D convertToDTO(E entity, Class<D> dtoClass) {
		return MAPPER.map(entity, dtoClass);
	}

	public static <E, D> E convertToEntity(D dto, Class<E> entityClass) {
		return MAPPER.map(dto, entityClass);
	}

}
