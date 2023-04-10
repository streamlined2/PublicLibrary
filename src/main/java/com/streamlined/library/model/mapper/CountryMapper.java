package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Country;
import com.streamlined.library.model.dto.CountryDto;

@Component
public class CountryMapper {

	public CountryDto toDto(Country country) {
		return CountryDto.builder().id(country.getId()).name(country.getName()).build();
	}

	public Country toEntity(CountryDto dto) {
		return Country.builder().id(dto.getId()).name(dto.getName()).build();
	}

}
