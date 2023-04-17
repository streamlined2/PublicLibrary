package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Country;
import com.streamlined.library.model.dto.CountryDto;

@Component
public class CountryMapper implements Mapper<CountryDto, Country> {

	public CountryDto toDto(Country entity) {
		return CountryDto.builder().id(entity.getId()).name(entity.getName()).build();
	}

	public Country toEntity(CountryDto dto) {
		return Country.builder().id(dto.getId()).name(dto.getName()).build();
	}

}
