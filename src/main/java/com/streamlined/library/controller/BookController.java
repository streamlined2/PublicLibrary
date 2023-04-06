package com.streamlined.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	@GetMapping("/getall")
	public String getAll() {
		return "books";
	}

}
