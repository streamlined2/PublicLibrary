package com.streamlined.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.streamlined.library.model.dto.BookDto;
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

	@GetMapping("/select")
	public String selectBooks() {
		return "select-books";
	}

	@GetMapping("/browse")
	public String browseBooks() {
		return "browse-books";
	}

	@GetMapping("/edit/{id}")
	public String editBook(@PathVariable Long id, Model model) {
		model.addAttribute("bookId", id);
		return "edit-book";
	}

	@GetMapping("/delete/{id}")
	public String deleteBook(@PathVariable Long id, Model model) {
		model.addAttribute("bookId", id);
		return "delete-book";
	}

	@GetMapping("/add")
	public String addBook() {
		return "add-book";
	}

}
