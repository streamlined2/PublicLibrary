package com.streamlined.library.model.dto;

import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Country;
import com.streamlined.library.model.Cover;

import lombok.Builder;

@Builder
public record CategoryDataDto(String value, long requests, long time) {

	public CategoryDataDto(Genre genre, long requests) {
		this(genre.name(), requests, 0L);
	}

	public CategoryDataDto(Country country, long requests) {
		this(country.getName(), requests, 0L);
	}

	public CategoryDataDto(int publishYear, long requests) {
		this(String.valueOf(publishYear), requests, 0L);
	}

	public CategoryDataDto(Size size, long requests) {
		this(size.name(), requests, 0L);
	}

	public CategoryDataDto(Cover.Type type, long requests) {
		this(type.name(), requests, 0L);
	}

	public CategoryDataDto(Cover.Surface surface, long requests) {
		this(surface.name(), requests, 0L);
	}

}
