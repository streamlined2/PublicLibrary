package com.streamlined.library.service;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ReviewRepository;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReviewDto;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.ReviewMapper;

import static com.streamlined.library.Utilities.toStream;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewMapper reviewMapper;
	private final BookMapper bookMapper;

	public Stream<BookDto> getReceivedBooks(Customer customer) {
		return toStream(reviewRepository.getReceivedBooks(customer)).map(bookMapper::toDto);
	}

	public Stream<ReviewDto> getBookReviews(Long bookId) {
		return toStream(reviewRepository.getBookReviews(bookId)).map(reviewMapper::toDto);
	}

	public ReviewDto getReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId).map(reviewMapper::toDto)
				.orElseThrow(() -> new NoEntityFoundException("no review found with id %d".formatted(reviewId)));
	}

}
