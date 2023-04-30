package com.streamlined.library.model.dto;

import java.time.LocalDate;

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
	
	public String getShortDescription() {
		return "%s, %s".formatted(author, title);
	}

}
