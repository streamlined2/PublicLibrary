package com.streamlined.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.streamlined.library.model.dto.ReviewDto;
import com.streamlined.library.service.BookService;
import com.streamlined.library.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

	private final BookService bookService;
	private final ReviewService reviewService;

	@ModelAttribute(name = "ratingList")
	public List<String> ratingList() {
		return reviewService.getRatingList().toList();
	}

	@GetMapping("/view")
	public String selectBookToViewReview(Model model) {
		model.addAttribute("bookList", bookService.getAllBooks());
		return "browse-books-for-review";
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

	@GetMapping("/submit")
	public String selectBookToSubmitReview(Model model) {
		model.addAttribute("receivedBooks", reviewService.getReceivedBooks());
		return "browse-received-books";
	}

	@GetMapping("/addedit/{bookId}")
	public String submitBookReview(@PathVariable Long bookId, Model model) {
		var review = reviewService.getBookReview(bookId).orElse(reviewService.getBlankReview(bookId));
		model.addAttribute("review", review);
		return "add-edit-review";
	}

	@PostMapping("/addedit/{bookId}")
	public String saveBookReview(@PathVariable Long bookId, ReviewDto reviewDto) {
		reviewService.saveReview(bookId, reviewDto);
		return "redirect:/review/submit";
	}

}