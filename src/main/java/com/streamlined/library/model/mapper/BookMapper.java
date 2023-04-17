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
public class BookMapper implements Mapper<BookDto, Book> {

	private final CountryMapper countryMapper;
	private final LanguageMapper languageMapper;

	public BookDto toDto(Book entity) {
		return BookDto.builder().id(entity.getId()).author(entity.getAuthor()).title(entity.getTitle()).isbn(entity.getIsbn())
				.publishDate(entity.getPublishDate()).genre(entity.getGenre().name())
				.country(countryMapper.toDto(entity.getCountry())).language(languageMapper.toDto(entity.getLanguage()))
				.pageCount(entity.getPageCount()).size(entity.getSize().name()).coverType(entity.getCover().getType().name())
				.coverSurface(entity.getCover().getSurface().name()).build();
	}

	public Book toEntity(BookDto dto) {
		return Book.builder().id(dto.getId()).author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn())
				.publishDate(dto.getPublishDate()).genre(Genre.valueOf(dto.getGenre()))
				.country(countryMapper.toEntity(dto.getCountry())).language(languageMapper.toEntity(dto.getLanguage()))
				.pageCount(dto.getPageCount()).size(Size.valueOf(dto.getSize()))
				.cover(new Cover(dto.getCoverType(), dto.getCoverSurface())).build();
	}

}
