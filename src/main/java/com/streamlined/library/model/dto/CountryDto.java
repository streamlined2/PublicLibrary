package com.streamlined.library.model.dto;

import com.streamlined.library.model.Country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

	private Long id;
	private String name;

	public static CountryDto create(Country country) {
		return builder().id(country.getId()).name(country.getName()).build();
	}

	public Country getEntity() {
		return Country.builder().id(id).name(name).build();
	}

}
