package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.streamlined.library.Utilities.toStream;

import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.CountryRepository;
import com.streamlined.library.dao.LanguageRepository;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Cover;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.LanguageDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BookService {

	private final BookRepository bookRepository;
	private final CountryRepository countryRepository;
	private final LanguageRepository languageRepository;

	public Stream<BookDto> getAll() {
		return toStream(bookRepository.findAll(Sort.unsorted())).map(BookDto::create);
	}

	public Optional<BookDto> findById(Long id) {
		return bookRepository.findById(id).map(BookDto::create);
	}

	@Transactional
	public void removeById(Long id) {
		bookRepository.deleteById(id);
	}

	@Transactional
	public void save(BookDto book) {
		bookRepository.save(book.getEntity());
	}

	public Stream<CountryDto> getAllCountries() {
		return toStream(countryRepository.findAll()).map(CountryDto::create);
	}

	public Stream<LanguageDto> getAllLanguages() {
		return toStream(languageRepository.findAll()).map(LanguageDto::create);
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

}
