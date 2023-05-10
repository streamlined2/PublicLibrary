package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.FilterKeyValueDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.model.dto.SortOrderDto;

public interface BookService {

	Page<BookDto> getAllBooks(Optional<Integer> page, Optional<SortOrderDto> sortOrder,
			FilterKeyValueDto filterKeyValue);
	
	Page<BookDto> getAllBooks(Optional<Integer> page, Optional<SortOrderDto> sortOrder);

	Stream<BookDto> getAvailableBooks();

	Optional<BookDto> findById(Long id);

	BookDto create();

	void removeById(Long id);

	void save(BookDto book);

	Stream<CountryDto> getAllCountries();

	Stream<LanguageDto> getAllLanguages();

	Stream<String> getAllGenres();

	Stream<String> getAllSizes();

	Stream<String> getAllCoverTypes();

	Stream<String> getAllCoverSurfaces();

	Stream<BookDto> getCustomerBooks(Long customerId);

}
