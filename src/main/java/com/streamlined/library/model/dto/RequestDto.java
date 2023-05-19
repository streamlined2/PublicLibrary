package com.streamlined.library.model.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Builder;

@Builder
public record RequestDto(Long id, @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") LocalDateTime createdTime,
		CustomerDto customer, Set<BookDto> books) {

	public RequestDto {
		books = new HashSet<>();
	}
	
	public void addBook(BookDto book) {
		books.add(book);
	}

}
