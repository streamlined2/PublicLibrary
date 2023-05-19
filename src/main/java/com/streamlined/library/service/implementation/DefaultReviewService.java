package com.streamlined.library.service.implementation;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.dao.ReviewRepository;
import com.streamlined.library.model.Review;
import com.streamlined.library.model.Review.Rating;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReviewDto;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.CustomerMapper;
import com.streamlined.library.model.mapper.ReviewMapper;
import com.streamlined.library.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReviewService implements ReviewService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final ReviewMapper reviewMapper;
	private final BookMapper bookMapper;

	@Override
	public Stream<BookDto> getReceivedBooks(String customerLogin) {
		return reviewRepository.getReceivedBooks(customerLogin).map(bookMapper::toDto).stream();
	}

	@Override
	public Stream<ReviewDto> getBookReviews(Long bookId) {
		return reviewRepository.getBookReviews(bookId).map(reviewMapper::toDto).stream();
	}

	@Override
	public Optional<ReviewDto> getBookReview(Long bookId, String customerLogin) {
		return reviewRepository.getBookReview(bookId, customerLogin).map(reviewMapper::toDto);
	}

	@Override
	public ReviewDto getBlankReview(Long bookId, String customerLogin) {
		var customer = customerRepository.findByLogin(customerLogin).orElseThrow(
				() -> new NoEntityFoundException("no customer found with login %s".formatted(customerLogin)));
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with id %d".formatted(bookId)));
		return ReviewDto.builder().book(bookMapper.toDto(book)).customer(customerMapper.toDto(customer))
				.rating(Rating.EXCELLENT).text("").build();
	}

	@Override
	public ReviewDto getReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId).map(reviewMapper::toDto)
				.orElseThrow(() -> new NoEntityFoundException("no review found with id %d".formatted(reviewId)));
	}

	@Override
	public Stream<String> getRatingList() {
		return Stream.of(Review.Rating.values()).map(Review.Rating::name);
	}

	@Transactional
	@Override
	public void saveReview(Long bookId, ReviewDto reviewDto, String customerLogin) {
		var review = reviewRepository.getBookReview(bookId, customerLogin).orElse(blankReview(bookId));
		review.setRating(reviewDto.rating());
		review.setText(reviewDto.text());
		reviewRepository.save(review);
	}

	private Review blankReview(Long bookId) {
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book entity with id %d".formatted(bookId)));
		return Review.builder().book(book).build();
	}

}
