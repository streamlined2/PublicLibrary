package com.streamlined.library.model.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Builder;

@Builder
public record ReturnDto(Long id, CustomerDto customer, LibrarianDto librarian, LocalDateTime createdTime,
		Set<BookDto> books) {

}
