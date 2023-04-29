package com.streamlined.library.model.dto;

import com.streamlined.library.model.Book.Genre;
import lombok.Builder;

@Builder
public record CustomerRequestDataDto(String genre, String language, String country, long count) { 

	public CustomerRequestDataDto(Genre genre, String language, String country, long count) {
		this(genre.name(), language, country, count);
	}

}
