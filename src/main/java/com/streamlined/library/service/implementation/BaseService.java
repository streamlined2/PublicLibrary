package com.streamlined.library.service.implementation;

import java.util.stream.Stream;

import com.streamlined.library.model.dto.CategoryDto;

public abstract class BaseService {

	public Stream<CategoryDto> getCategories() {
		return Stream.of(new CategoryDto("genre", "Genre"), new CategoryDto("country", "Country"),
				new CategoryDto("language", "Language"), new CategoryDto("publish-year", "Publish year"),
				new CategoryDto("size", "Size"), new CategoryDto("cover-type", "Cover type"),
				new CategoryDto("cover-surface", "Cover surface"));
	}

}
