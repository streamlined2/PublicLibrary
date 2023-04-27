package com.streamlined.library.model.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Cover;

import lombok.Builder;

@Builder
public record CategoryTimeDataDto(String value, BigDecimal durationNanoseconds) {

	public CategoryTimeDataDto(Genre genre, BigDecimal durationNanoseconds) {
		this(genre.name(), durationNanoseconds);
	}

	public CategoryTimeDataDto(int year, BigDecimal durationNanoseconds) {
		this(String.valueOf(year), durationNanoseconds);
	}

	public CategoryTimeDataDto(Size size, BigDecimal durationNanoseconds) {
		this(size.name(), durationNanoseconds);
	}

	public CategoryTimeDataDto(Cover.Type type, BigDecimal durationNanoseconds) {
		this(type.name(), durationNanoseconds);
	}

	public CategoryTimeDataDto(Cover.Surface surface, BigDecimal durationNanoseconds) {
		this(surface.name(), durationNanoseconds);
	}

	public String duration() {
		var duration = Duration.of(durationNanoseconds.longValue(), ChronoUnit.NANOS);
		return "%2d day(s), %2d hour(s)".formatted(duration.toDays(), duration.toHoursPart());
	}

}
