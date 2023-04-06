package com.streamlined.library.model.dto;

import java.time.LocalDate;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Cover;

public record BookDto(Long id, String author, String title, String isbn, LocalDate publishDate, String genre,
		CountryDto country, LanguageDto language, int pageCount, String size, String coverType, String coverSurface) {

	public BookDto(Book book) {
		this(book.getId(), book.getAuthor(), book.getTitle(), book.getIsbn(), book.getPublishDate(),
				book.getGenre().name(), new CountryDto(book.getCountry()), new LanguageDto(book.getLanguage()),
				book.getPageCount(), book.getSize().name(), book.getCover().getType().name(),
				book.getCover().getSurface().name());
	}

	public Book getEntity() {
		return Book.builder().id(id).author(author).title(title).isbn(isbn).publishDate(publishDate)
				.genre(Genre.valueOf(genre)).country(country.getEntity()).language(language.getEntity())
				.pageCount(pageCount).size(Size.valueOf(size)).cover(new Cover(coverType, coverSurface)).build();
	}

}
