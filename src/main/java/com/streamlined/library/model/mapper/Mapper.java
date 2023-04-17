package com.streamlined.library.model.mapper;

public interface Mapper<D, E> {

	E toEntity(D dto);

	D toDto(E entity);

}
