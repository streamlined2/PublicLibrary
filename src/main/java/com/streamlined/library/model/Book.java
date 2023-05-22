package com.streamlined.library.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Comparator;

import org.hibernate.validator.constraints.ISBN;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "author", "title", "publish_date", "country", "language" }) })
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Book implements Comparable<Book> {

	private static final Comparator<Book> BY_AUTHOR_TITLE_PUBLISH_DATE_COMPARATOR = Comparator
			.comparing(Book::getAuthor).thenComparing(Book::getTitle).thenComparing(Book::getPublishDate);

	public enum Genre {
		SCIENTIFICAL, EDUCATIONAL, FICTIONAL, HISTORICAL, BIOGRAPHICAL, PHILOSOPHICAL
	}

	public enum Size {
		FOLIO, QUARTO, OCTAVO, DUODECIMO
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@NotBlank(message = "empty author is not valid")
	@Column(name = "author", nullable = false, unique = false)
	private String author;

	@NotBlank(message = "empty title is not valid")
	@Column(name = "title", nullable = false, unique = false)
	private String title;

	@ISBN
	@Column(name = "isbn", nullable = false, unique = true)
	private String isbn;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "publish_date", nullable = false, unique = false)
	private LocalDate publishDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "genre", nullable = false, unique = false)
	private Genre genre;

	@ManyToOne(optional = false)
	@JoinColumn(name = "country", nullable = false, unique = false)
	private Country country;

	@ManyToOne(optional = false)
	@JoinColumn(name = "language", nullable = false, unique = false)
	private Language language;

	@Positive
	@Column(name = "page_count", nullable = false, unique = false)
	private int pageCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "size", nullable = false, unique = false)
	private Size size;

	@AttributeOverride(name = "type", column = @Column(name = "cover_type", nullable = false, unique = false))
	@AttributeOverride(name = "surface", column = @Column(name = "cover_surface", nullable = false, unique = false))
	private Cover cover;

	@Override
	public int compareTo(Book o) {
		if (o == null) {
			throw new IllegalArgumentException("book to compare with should not be null");
		}
		return BY_AUTHOR_TITLE_PUBLISH_DATE_COMPARATOR.compare(this, o);
	}

}
