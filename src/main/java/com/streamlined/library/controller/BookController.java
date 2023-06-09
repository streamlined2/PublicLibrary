package com.streamlined.library.controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.FilterKeyValueDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.model.dto.PageNavigationDto;
import com.streamlined.library.model.dto.SortOrderDto;
import com.streamlined.library.service.BookService;
import static com.streamlined.library.Utilities.getSourceForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/book")
@SessionAttributes({ "sortOrder", "filterKeyValues" })
@RequiredArgsConstructor
public class BookController {

	private static final String ORIGINAL_FORM_URI = "originalFormURI";

	private final BookService bookService;

	@ModelAttribute(name = "sortOrder")
	public SortOrderDto sortOrder() {
		return SortOrderDto.create();
	}

	@ModelAttribute(name = "filterKeyValues")
	public FilterKeyValueDto filterKeyValues() {
		return new FilterKeyValueDto();
	}

	@ModelAttribute(name = "genreList")
	public List<String> genreList() {
		return bookService.getAllGenres().toList();
	}

	@ModelAttribute(name = "countryList")
	public List<CountryDto> countryList() {
		return bookService.getAllCountries().toList();
	}

	@ModelAttribute(name = "languageList")
	public List<LanguageDto> languageList() {
		return bookService.getAllLanguages().toList();
	}

	@ModelAttribute(name = "sizeList")
	public List<String> sizeList() {
		return bookService.getAllSizes().toList();
	}

	@ModelAttribute(name = "coverTypeList")
	public List<String> coverTypeList() {
		return bookService.getAllCoverTypes().toList();
	}

	@ModelAttribute(name = "coverSurfaceList")
	public List<String> coverSurfaceList() {
		return bookService.getAllCoverSurfaces().toList();
	}

	@GetMapping("/select")
	public String selectBooks(Model model) {
		model.addAttribute("availableBooks", bookService.getAvailableBooks().toList());
		return "select-books";
	}

	@GetMapping("/browse")
	public String browseBooks(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@ModelAttribute("sortOrder") Optional<SortOrderDto> sortOrder,
			@ModelAttribute("filterKeyValues") FilterKeyValueDto filterKeyValue, Model model) {

		var books = bookService.getAllBooks(page, sortOrder, filterKeyValue);
		model.addAttribute("navigation", new PageNavigationDto(books.getTotalPages(), page));
		model.addAttribute("bookList", books.toList());
		return "browse-books";
	}

	@GetMapping("/apply-filter")
	public String applyFilter(@ModelAttribute("filterKeyValues") FilterKeyValueDto filterKeyValue,
			@RequestParam(ORIGINAL_FORM_URI) String originalFormURI, @RequestParam Map<String, String> parameters) {
		filterKeyValue.updateKeyValues(parameters);
		return "redirect:" + originalFormURI;
	}

	@GetMapping("/discard-filter")
	public String discardFilter(@ModelAttribute("filterKeyValues") FilterKeyValueDto filterKeyValue,
			@RequestParam(ORIGINAL_FORM_URI) String originalFormURI) {
		filterKeyValue.clear();
		return "redirect:" + originalFormURI;
	}

	@GetMapping("/add")
	public String addBook(Model model) {
		model.addAttribute("book", bookService.create());
		return "add-book";
	}

	@PostMapping("/new")
	public String createBook(BookDto book) {
		bookService.save(book);
		return "redirect:/book/browse";
	}

	@GetMapping("/edit/{id}")
	public String editBook(@PathVariable Long id, Model model) {
		model.addAttribute("book", bookService.findById(id)
				.orElseThrow(() -> new NoEntityFoundException("no book found for id %d".formatted(id))));
		return "edit-book";
	}

	@PostMapping("/edit/{id}")
	public String saveBook(@PathVariable Long id, BookDto book) {
		bookService.save(book);
		return "redirect:/book/browse";
	}

	@GetMapping("/delete/{id}")
	public String deleteBook(@PathVariable Long id, Model model) {
		model.addAttribute("book", bookService.findById(id)
				.orElseThrow(() -> new NoEntityFoundException("no book found for id %d".formatted(id))));
		return "delete-book";
	}

	@PostMapping("/delete/{id}")
	public String removeBook(@PathVariable Long id) {
		bookService.removeById(id);
		return "redirect:/book/browse";
	}

	@GetMapping("/find-holder")
	public String findBookHolder(@RequestParam(name = "page", required = false) Optional<Integer> page,
			@ModelAttribute("sortOrder") Optional<SortOrderDto> sortOrder,
			@ModelAttribute("filterKeyValues") FilterKeyValueDto filterKeyValue, Model model) {
		var books = bookService.getAllBooks(page, sortOrder, filterKeyValue);
		model.addAttribute("navigation", new PageNavigationDto(books.getTotalPages(), page));
		model.addAttribute("bookList", books.toList());
		return "browse-books-for-holder";
	}

	@GetMapping("/customer-books/{customerId}")
	public String showCustomerBooks(@PathVariable Long customerId, Model model) {
		model.addAttribute("customerBooks", bookService.getCustomerBooks(customerId));
		return "view-customer-books";
	}

	@GetMapping("/filter")
	public String displayFilterForm(WebRequest request, Model model) {
		model.addAttribute("book", bookService.create());
		model.addAttribute("returnLink", getSourceForm(request));
		return "filter-book";
	}

	@GetMapping("/logout")
	public String closeSession(SessionStatus sessioStatus) {
		sessioStatus.setComplete();
		return "redirect:/";
	}

}
