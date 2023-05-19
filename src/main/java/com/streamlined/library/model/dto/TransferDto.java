package com.streamlined.library.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;

@Builder
public record TransferDto(Long id, ApprovalDto approval, LibrarianDto librarian,
		@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") LocalDateTime createdTime, Set<BookDto> books) {

	public TransferDto {
		books = new HashSet<>();
	}

	public void addBook(BookDto book) {
		books.add(book);
	}

}
