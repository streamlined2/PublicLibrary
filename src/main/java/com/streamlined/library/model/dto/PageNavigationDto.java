package com.streamlined.library.model.dto;

import java.util.Optional;

import org.springframework.data.domain.Page;

public record PageNavigationDto(int first, int last, int next, int previous) {

	private static final int FIRST_PAGE = 0;

	public PageNavigationDto(Page<BookDto> books, Optional<Integer> page) {
		this(FIRST_PAGE, lastPageNumber(books), nextPageNumber(books, page), previousPageNumber(page));
	}

	private static int lastPageNumber(Page<BookDto> books) {
		return books.getTotalPages() - 1;
	}

	private static int nextPageNumber(Page<BookDto> books, Optional<Integer> page) {
		var lastPage = lastPageNumber(books);
		return page.map(num -> Math.min(num + 1, lastPage)).orElse(Math.min(FIRST_PAGE + 1, lastPage));
	}

	private static int previousPageNumber(Optional<Integer> page) {
		return page.map(num -> Math.max(num - 1, FIRST_PAGE)).orElse(FIRST_PAGE);
	}

}
