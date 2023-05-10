package com.streamlined.library.service.implementation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.ParseException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.CountryRepository;
import com.streamlined.library.dao.LanguageRepository;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Cover;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.FilterKeyValueDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.model.dto.SortOrderDto;
import com.streamlined.library.model.dto.converter.StringToCountryDtoConverter;
import com.streamlined.library.model.dto.converter.StringToLanguageDtoConverter;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.CountryMapper;
import com.streamlined.library.model.mapper.LanguageMapper;
import com.streamlined.library.service.BookService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DefaultBookService implements BookService {

	private static final Set<String> EXCLUDE_PATHS = Set.of("pageCount", "type", "surface");

	private final BookRepository bookRepository;
	private final CountryRepository countryRepository;
	private final LanguageRepository languageRepository;
	private final BookMapper bookMapper;
	private final CountryMapper countryMapper;
	private final LanguageMapper languageMapper;
	private final StringToCountryDtoConverter stringToCountryDtoConverter;
	private final StringToLanguageDtoConverter stringToLanguageDtoConverter;

	private @Value("${book.page.size}") int bookPageSize;

	public Page<BookDto> getAllBooks(Optional<Integer> page, Optional<SortOrderDto> sortOrder,
			FilterKeyValueDto filterKeyValue) {

		Sort sort = Sort.by(SortOrderDto.getOrderByParameter(sortOrder));
		Pageable pageable = PageRequest.of(page.orElse(0), bookPageSize).withSort(sort);
		Book book = createBook(filterKeyValue);
		Example<Book> example = Example.of(book, getMatcher(filterKeyValue));
		return bookRepository.findAll(example, pageable).map(bookMapper::toDto);
	}

	public Page<BookDto> getAllBooks(Optional<Integer> page, Optional<SortOrderDto> sortOrder) {
		Sort sort = Sort.by(SortOrderDto.getOrderByParameter(sortOrder));
		Pageable pageable = PageRequest.of(page.orElse(0), bookPageSize).withSort(sort);
		return bookRepository.findAll(pageable).map(bookMapper::toDto);
	}

	private ExampleMatcher getMatcher(FilterKeyValueDto filterKeyValue) {
		Set<String> ignorePaths = new HashSet<>();
		ignorePaths.addAll(filterKeyValue.getIgnorePaths());
		ignorePaths.addAll(EXCLUDE_PATHS);
		return ExampleMatcher.matchingAll().withIgnorePaths(ignorePaths.toArray(new String[0])).withIgnoreNullValues()
				.withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
	}

	private Book createBook(FilterKeyValueDto filterKeyValue) {
		var builder = Book.builder();
		for (var entry : filterKeyValue) {
			switch (entry.getKey()) {
			case "author" -> builder.author(entry.getValue());
			case "title" -> builder.title(entry.getValue());
			case "isbn" -> builder.isbn(entry.getValue());
			case "publishDate" -> builder.publishDate(LocalDate.parse(entry.getValue()));
			case "genre" -> builder.genre(Genre.valueOf(entry.getValue()));
			case "country" -> {
				var countryDto = stringToCountryDtoConverter.convert(entry.getValue());
				builder.country(countryMapper.toEntity(countryDto));
			}
			case "language" -> {
				var languageDto = stringToLanguageDtoConverter.convert(entry.getValue());
				builder.language(languageMapper.toEntity(languageDto));
			}
			case "pageCount" -> builder.pageCount(Integer.valueOf(entry.getValue()));
			case "size" -> builder.size(Size.valueOf(entry.getValue()));
			default -> throw new ParseException("incorrect id for book field %s".formatted(entry.getKey()));
			}
		}
		return builder.build();
	}

	public Stream<BookDto> getAvailableBooks() {// TODO should be elaborated later to skip already requested books
		return Streamable.of(bookRepository.findAll(Sort.unsorted())).map(bookMapper::toDto).stream();
	}

	public Optional<BookDto> findById(Long id) {
		return bookRepository.findById(id).map(bookMapper::toDto);
	}

	public BookDto create() {
		return new BookDto();
	}

	@Transactional
	public void removeById(Long id) {
		bookRepository.deleteById(id);
	}

	@Transactional
	public void save(BookDto book) {
		bookRepository.save(bookMapper.toEntity(book));
	}

	public Stream<CountryDto> getAllCountries() {
		return Streamable.of(countryRepository.findAll()).map(countryMapper::toDto).stream();
	}

	public Stream<LanguageDto> getAllLanguages() {
		return Streamable.of(languageRepository.findAll()).map(languageMapper::toDto).stream();
	}

	public Stream<String> getAllGenres() {
		return Stream.of(Book.Genre.values()).map(Book.Genre::name);
	}

	public Stream<String> getAllSizes() {
		return Stream.of(Book.Size.values()).map(Book.Size::name);
	}

	public Stream<String> getAllCoverTypes() {
		return Stream.of(Cover.Type.values()).map(Cover.Type::name);
	}

	public Stream<String> getAllCoverSurfaces() {
		return Stream.of(Cover.Surface.values()).map(Cover.Surface::name);
	}

	public Stream<BookDto> getCustomerBooks(Long customerId) {
		var firstCustomerTransferIterator = bookRepository.getCustomerTransfers(customerId).iterator();
		if (firstCustomerTransferIterator.hasNext()) {
			return firstCustomerTransferIterator.next().getBooks().stream().map(bookMapper::toDto);
		}
		return Stream.empty();
	}

}
