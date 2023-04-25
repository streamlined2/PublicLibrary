package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.ReviewRepository;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Review;
import com.streamlined.library.model.Review.Rating;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReviewDto;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.CustomerMapper;
import com.streamlined.library.model.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final ReviewMapper reviewMapper;
	private final BookMapper bookMapper;
	private final CustomerMapper customerMapper;
	private final Customer customer;// TODO should be replaced with authenticated user from security context

	public Stream<BookDto> getReceivedBooks() {
		return reviewRepository.getReceivedBooks(customer).map(bookMapper::toDto).stream();
	}

	public Stream<ReviewDto> getBookReviews(Long bookId) {
		return reviewRepository.getBookReviews(bookId).map(reviewMapper::toDto).stream();
	}

	public Optional<ReviewDto> getBookReview(Long bookId) {
		return reviewRepository.getBookReview(bookId, customer).map(reviewMapper::toDto);
	}

	public ReviewDto getBlankReview(Long bookId) {
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with id %d".formatted(bookId)));
		return ReviewDto.builder().book(bookMapper.toDto(book)).customer(customerMapper.toDto(customer))
				.rating(Rating.EXCELLENT).text("").build();
	}

	public ReviewDto getReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId).map(reviewMapper::toDto)
				.orElseThrow(() -> new NoEntityFoundException("no review found with id %d".formatted(reviewId)));
	}

	public Stream<String> getRatingList() {
		return Stream.of(Review.Rating.values()).map(Review.Rating::name);
	}

	@Transactional
	public void saveReview(Long bookId, ReviewDto reviewDto) {
		var review = reviewRepository.getBookReview(bookId, customer).orElse(blankReview(bookId));
		review.setRating(reviewDto.rating());
		review.setText(reviewDto.text());
		reviewRepository.save(review);
	}

	private Review blankReview(Long bookId) {
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book entity with id %d".formatted(bookId)));
		return Review.builder().book(book).customer(customer).build();
	}

}
