package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReviewDto;

public interface ReviewService {
	
	Stream<BookDto> getReceivedBooks();
	
	Stream<ReviewDto> getBookReviews(Long bookId);
	
	Optional<ReviewDto> getBookReview(Long bookId);
	
	ReviewDto getBlankReview(Long bookId);
	
	ReviewDto getReviewById(Long reviewId);
	
	Stream<String> getRatingList();
	
	void saveReview(Long bookId, ReviewDto reviewDto);
	
}
