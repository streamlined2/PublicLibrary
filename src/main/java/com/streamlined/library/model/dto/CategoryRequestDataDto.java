package com.streamlined.library.model.dto;

import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Country;
import com.streamlined.library.model.Cover;

import lombok.Builder;

@Builder
public record CategoryRequestDataDto(String value, long requests) {

	public CategoryRequestDataDto(Genre genre, long requests) {
		this(genre.name(), requests);
	}

	public CategoryRequestDataDto(Country country, long requests) {
		this(country.getName(), requests);
	}

	public CategoryRequestDataDto(int publishYear, long requests) {
		this(String.valueOf(publishYear), requests);
	}

	public CategoryRequestDataDto(Size size, long requests) {
		this(size.name(), requests);
	}

	public CategoryRequestDataDto(Cover.Type type, long requests) {
		this(type.name(), requests);
	}

	public CategoryRequestDataDto(Cover.Surface surface, long requests) {
		this(surface.name(), requests);
	}

}
