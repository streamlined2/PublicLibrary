package com.streamlined.library.model.dto;

import java.time.LocalDateTime;

import com.streamlined.library.model.Review.Rating;

import lombok.Builder;

@Builder
public record ReviewDto(Long id, BookDto book, CustomerDto customer, LocalDateTime createdTime,
		LocalDateTime updatedTime, Rating rating, String text) {

}
