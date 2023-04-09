package com.streamlined.library.model.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Cover;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
	private Long id;
	private String author;
	private String title;
	private String isbn;
	private @DateTimeFormat(iso = ISO.DATE) LocalDate publishDate;
	private String genre;
	private CountryDto country;
	private LanguageDto language;
	private int pageCount;
	private String size;
	private String coverType;
	private String coverSurface;

	public static BookDto create(Book book) {
		return builder().id(book.getId()).author(book.getAuthor()).title(book.getTitle()).isbn(book.getIsbn())
				.publishDate(book.getPublishDate()).genre(book.getGenre().name())
				.country(CountryDto.create(book.getCountry())).language(LanguageDto.create(book.getLanguage()))
				.pageCount(book.getPageCount()).size(book.getSize().name()).coverType(book.getCover().getType().name())
				.coverSurface(book.getCover().getSurface().name()).build();
	}

	public Book getEntity() {
		return Book.builder().id(id).author(author).title(title).isbn(isbn).publishDate(publishDate)
				.genre(Genre.valueOf(genre)).country(country.getEntity()).language(language.getEntity())
				.pageCount(pageCount).size(Size.valueOf(size)).cover(new Cover(coverType, coverSurface)).build();
	}

}
