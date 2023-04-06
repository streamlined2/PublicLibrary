package com.streamlined.library.model.dto;

import com.streamlined.library.model.Country;

public record CountryDto(Long id, String name) {

	public CountryDto(Country country) {
		this(country.getId(), country.getName());
	}

	public Country getEntity() {
		return Country.builder().id(id).name(name).build();
	}

}
