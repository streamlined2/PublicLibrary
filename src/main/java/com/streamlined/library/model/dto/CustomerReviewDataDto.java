package com.streamlined.library.model.dto;

import com.streamlined.library.model.Book.Genre;
import lombok.Builder;

@Builder
public record CustomerReviewDataDto(String genre, String language, String country, Double averageRating) {

	public CustomerReviewDataDto(Genre genre, String language, String country, Double averageRating) {
		this(genre.name(), language, country, averageRating);
	}

}
