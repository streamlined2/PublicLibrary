package com.streamlined.library.model.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ClaimDto(Long id, ReturnDto bookReturn, BookDto book, LibrarianDto librarian, LocalDateTime createdTime,
		String damageDescription) {

}
