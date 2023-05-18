package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.FilterKeyValueDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.model.dto.SortOrderDto;
import com.streamlined.library.security.IsCustomer;
import com.streamlined.library.security.IsCustomerOrLibrarian;
import com.streamlined.library.security.IsLibrarian;

@IsCustomerOrLibrarian
public interface BookService {

	Page<BookDto> getAllBooks(Optional<Integer> page, Optional<SortOrderDto> sortOrder,
			FilterKeyValueDto filterKeyValue);
	
	Page<BookDto> getAllBooks(Optional<Integer> page, Optional<SortOrderDto> sortOrder);

	@IsCustomer
	Stream<BookDto> getAvailableBooks();

	Optional<BookDto> findById(Long id);

	BookDto create();

	@IsLibrarian
	void removeById(Long id);

	@IsLibrarian
	void save(BookDto book);

	@IsLibrarian
	Stream<BookDto> getCustomerBooks(Long customerId);

	Stream<CountryDto> getAllCountries();

	Stream<LanguageDto> getAllLanguages();

	Stream<String> getAllGenres();

	Stream<String> getAllSizes();

	Stream<String> getAllCoverTypes();

	Stream<String> getAllCoverSurfaces();

}
