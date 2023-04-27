package com.streamlined.library.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.CategoryDto;

public abstract class BaseService {

	protected final int[] yearBoundary = { 50, 30, 20, 10 };

	protected List<LocalDate> getDateBoundary() {
		LocalDate presentDate = LocalDate.now();
		var boundary = new LinkedList<LocalDate>();
		for (var year : yearBoundary) {
			boundary.add(presentDate.minus(year, ChronoUnit.YEARS));
		}
		return List.copyOf(boundary);
	}

	public List<String> getDateBoundaryRepresentation() {
		var boundary = new LinkedList<String>();
		if (yearBoundary.length > 0) {
			boundary.add("older %d".formatted(yearBoundary[0]));
			for (int k = 0; k < yearBoundary.length - 1; k++) {
				boundary.add("between %d and %d".formatted(yearBoundary[k + 1], yearBoundary[k]));
			}
			boundary.add("younger %d".formatted(yearBoundary[yearBoundary.length - 1]));
		}
		return List.copyOf(boundary);
	}

	public Stream<CategoryDto> getCategories() {
		return Stream.of(new CategoryDto("genre", "Genre"), new CategoryDto("country", "Country"),
				new CategoryDto("language", "Language"), new CategoryDto("publish-year", "Publish year"),
				new CategoryDto("size", "Size"), new CategoryDto("cover-type", "Cover type"),
				new CategoryDto("cover-surface", "Cover surface"));
	}

}
