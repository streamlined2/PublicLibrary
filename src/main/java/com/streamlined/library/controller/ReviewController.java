package com.streamlined.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.streamlined.library.model.Customer;
import com.streamlined.library.service.BookService;
import com.streamlined.library.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

	private final BookService bookService;
	private final ReviewService reviewService;
	private final Customer customer;// TODO should be replaced with authenticated customer from security context

	@GetMapping("/select")
	public String selectBookToReview(Model model) {
		model.addAttribute("receivedBooks", reviewService.getReceivedBooks(customer));
		return "browse-received-books";
	}

	@GetMapping("/browse/{bookId}")
	public String browseBookReviews(@PathVariable Long bookId, Model model) {
		model.addAttribute("book", bookService.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with id %d".formatted(bookId))));
		model.addAttribute("bookReviews", reviewService.getBookReviews(bookId));
		return "browse-book-reviews";
	}

	@GetMapping("/details/{reviewId}")
	public String viewReviewDetails(@PathVariable Long reviewId, Model model) {
		model.addAttribute("review", reviewService.getReviewById(reviewId));
		return "view-review-details";
	}

}
