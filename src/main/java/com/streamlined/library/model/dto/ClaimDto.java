package com.streamlined.library.model.dto;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

import lombok.Builder;

@Builder
public record ClaimDto(Long id, ReturnDto bookReturn, BookDto book, LocalDateTime createdTime,
		String damageDescription, MonetaryAmount compensation) {

}
