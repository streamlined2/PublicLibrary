package com.streamlined.library.model.dto;

import java.util.Optional;

public record PageNavigationDto(int current, int first, int last, int next, int previous) {

	private static final int FIRST_PAGE = 0;

	public PageNavigationDto(int totalPages, Optional<Integer> page) {
		this(currentPageNumber(page), FIRST_PAGE, lastPageNumber(totalPages), nextPageNumber(totalPages, page),
				previousPageNumber(page));
	}

	private static int currentPageNumber(Optional<Integer> page) {
		return page.orElse(FIRST_PAGE);
	}

	private static int lastPageNumber(int totalPages) {
		return totalPages - 1;
	}

	private static int nextPageNumber(int totalPages, Optional<Integer> page) {
		return Math.min(currentPageNumber(page) + 1, lastPageNumber(totalPages));
	}

	private static int previousPageNumber(Optional<Integer> page) {
		return Math.max(currentPageNumber(page) - 1, FIRST_PAGE);
	}

}
