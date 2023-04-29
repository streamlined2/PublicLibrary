package com.streamlined.library.model.dto;

import java.math.BigDecimal;
import static com.streamlined.library.Utilities.convertNanosecondsToDaysHoursString;

import com.streamlined.library.model.Book.Genre;
import lombok.Builder;

@Builder
public record CustomerTimeDataDto(String genre, String language, String country, BigDecimal durationNanoseconds) {

	public CustomerTimeDataDto(Genre genre, String language, String country, BigDecimal durationNanoseconds) {
		this(genre.name(), language, country, durationNanoseconds);
	}

	public String duration() {
		return convertNanosecondsToDaysHoursString(durationNanoseconds);
	}

}
