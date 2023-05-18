package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReviewDto;
import com.streamlined.library.security.IsCustomer;
import com.streamlined.library.security.IsCustomerOrLibrarian;

@IsCustomerOrLibrarian
public interface ReviewService {

	@IsCustomer
	Stream<BookDto> getReceivedBooks(String customerLogin);

	Stream<ReviewDto> getBookReviews(Long bookId);

	Optional<ReviewDto> getBookReview(Long bookId, String customerLogin);

	ReviewDto getBlankReview(Long bookId, String customerLogin);

	ReviewDto getReviewById(Long reviewId);

	Stream<String> getRatingList();

	@IsCustomer
	void saveReview(Long bookId, ReviewDto reviewDto, String customerLogin);

}
