package com.streamlined.library.model.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import lombok.Builder;

@Builder
public record ApprovalDto(Long id, RequestDto request, LibrarianDto librarian,
		@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") LocalDateTime createdTime, Set<BookDto> books) {
}
