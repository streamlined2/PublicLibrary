package com.streamlined.library.model.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.streamlined.library.model.Review.Rating;

import lombok.Builder;

@Builder
public record ReviewDto(Long id, BookDto book, CustomerDto customer,
		@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") LocalDateTime createdTime,
		@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss") LocalDateTime updatedTime, Rating rating, String text) {

}
