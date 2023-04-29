package com.streamlined.library.model.dto;

import com.streamlined.library.model.Person.Sex;
import lombok.Builder;

@Builder
public record CustomerSummaryDataDto(Sex sex, String value, long count) {

	public String getSex() {
		return switch (sex) {
		case FEMALE -> "Female";
		case MALE -> "Male";
		default -> throw new IllegalArgumentException("wrong value for sex %s".formatted(sex.toString()));
		};
	}

}
