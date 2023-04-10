package com.streamlined.library.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.service.BookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@ModelAttribute(name = "bookList")
	public List<BookDto> bookList() {
		return bookService.getAll().toList();
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
	public String selectBooks() {
		return "select-books";
	}

	@GetMapping("/browse")
	public String browseBooks() {
		return "browse-books";
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
		var book = bookService.findById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "edit-book";
		}
		return "browse";
	}

	@PostMapping("/edit/{id}")
	public String saveBook(@PathVariable Long id, BookDto book) {
		bookService.save(book);
		return "redirect:/book/browse";
	}

	@GetMapping("/delete/{id}")
	public String deleteBook(@PathVariable Long id, Model model) {
		var book = bookService.findById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "delete-book";
		}
		return "browse";
	}

	@PostMapping("/delete/{id}")
	public String removeBook(@PathVariable Long id) {
		bookService.removeById(id);
		return "redirect:/book/browse";
	}

}