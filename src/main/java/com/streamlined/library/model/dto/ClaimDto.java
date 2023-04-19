package com.streamlined.library.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ClaimDto(Long id, CustomerDto customer, LibrarianDto librarian, BookDto book, LocalDateTime createdTime,
		String damageDescription, BigDecimal compensation) {

}
