package com.streamlined.library.service;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.model.dto.BookDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookService {

	private final BookRepository bookRepository;

	public Stream<BookDto> getAll() {
		return StreamSupport.stream(bookRepository.findAll(Sort.unsorted()).spliterator(), false).map(BookDto::new);
	}

}
