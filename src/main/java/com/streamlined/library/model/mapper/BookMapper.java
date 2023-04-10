package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Cover;
import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.dto.BookDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookMapper {

	private final CountryMapper countryMapper;
	private final LanguageMapper languageMapper;

	public BookDto toDto(Book book) {
		return BookDto.builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle()).isbn(book.getIsbn())
				.publishDate(book.getPublishDate()).genre(book.getGenre().name())
				.country(countryMapper.toDto(book.getCountry())).language(languageMapper.toDto(book.getLanguage()))
				.pageCount(book.getPageCount()).size(book.getSize().name()).coverType(book.getCover().getType().name())
				.coverSurface(book.getCover().getSurface().name()).build();
	}

	public Book toEntity(BookDto dto) {
		return Book.builder().id(dto.getId()).author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn())
				.publishDate(dto.getPublishDate()).genre(Genre.valueOf(dto.getGenre()))
				.country(countryMapper.toEntity(dto.getCountry()))
				.language(languageMapper.toEntity(dto.getLanguage())).pageCount(dto.getPageCount())
				.size(Size.valueOf(dto.getSize())).cover(new Cover(dto.getCoverType(), dto.getCoverSurface())).build();
	}

}
