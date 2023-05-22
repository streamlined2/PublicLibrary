package com.streamlined.library.model.dto;

import java.time.LocalDate;
import java.util.Comparator;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto implements Comparable<BookDto> {

	private static final Comparator<BookDto> BY_AUTHOR_TITLE_PUBLISH_DATE_COMPARATOR = Comparator
			.comparing(BookDto::getAuthor).thenComparing(BookDto::getTitle).thenComparing(BookDto::getPublishDate);

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

	public String getShortDescription() {
		return "%s, %s".formatted(author, title);
	}

	@Override
	public int compareTo(BookDto o) {
		if (o == null) {
			throw new IllegalArgumentException("book to compare with should not be null");
		}
		return BY_AUTHOR_TITLE_PUBLISH_DATE_COMPARATOR.compare(this, o);
	}

}
