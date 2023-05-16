package com.streamlined.library.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.streamlined.library.model.dto.FilterKeyValueDto;
import com.streamlined.library.model.dto.PageNavigationDto;
import com.streamlined.library.model.dto.ReviewDto;
import com.streamlined.library.model.dto.SortOrderDto;
import com.streamlined.library.service.BookService;
import com.streamlined.library.service.ReviewService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes({ "sortOrder", "filterKeyValues" })
@RequestMapping("/review")
public class ReviewController {

	private final BookService bookService;
	private final ReviewService reviewService;

	@ModelAttribute(name = "sortOrder")
	public SortOrderDto sortOrder() {
		return SortOrderDto.create();
	}

	@ModelAttribute(name = "filterKeyValues")
	public FilterKeyValueDto filterKeyValues() {
		return new FilterKeyValueDto();
	}

	@ModelAttribute(name = "ratingList")
	public List<String> ratingList() {
		return reviewService.getRatingList().toList();
	}

	@GetMapping("/view")
	public String selectBookToViewReview(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@ModelAttribute("sortOrder") Optional<SortOrderDto> sortOrder,
			@ModelAttribute("filterKeyValues") FilterKeyValueDto filterKeyValue, Model model) {
		var books = bookService.getAllBooks(page, sortOrder, filterKeyValue);
		model.addAttribute("navigation", new PageNavigationDto(books.getTotalPages(), page));
		model.addAttribute("bookList", books.toList());
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
	public String selectBookToSubmitReview(Model model, Principal principal) {
		model.addAttribute("receivedBooks", reviewService.getReceivedBooks(principal.getName()));
		return "browse-received-books";
	}

	@GetMapping("/addedit/{bookId}")
	public String submitBookReview(@PathVariable Long bookId, Model model, Principal principal) {
		var review = reviewService.getBookReview(bookId, principal.getName())
				.orElse(reviewService.getBlankReview(bookId, principal.getName()));
		model.addAttribute("review", review);
		return "add-edit-review";
	}

	@PostMapping("/addedit/{bookId}")
	public String saveBookReview(@PathVariable Long bookId, ReviewDto reviewDto, Principal principal) {
		reviewService.saveReview(bookId, reviewDto, principal.getName());
		return "redirect:/review/submit";
	}

}
