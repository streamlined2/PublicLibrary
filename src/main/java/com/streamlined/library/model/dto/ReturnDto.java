package com.streamlined.library.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;

@Builder
public record ReturnDto(Long id, CustomerDto customer, LibrarianDto librarian, LocalDateTime createdTime,
		Set<BookDto> books) {

	public ReturnDto {
		books = new HashSet<>();
	}

	public String getBookList() {
		return books.stream().map(BookDto::getTitle).collect(Collectors.joining(", "));
	}

	public void addBook(BookDto book) {
		books.add(book);
	}

}
