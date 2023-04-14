package com.streamlined.library.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

import org.hibernate.validator.constraints.ISBN;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Book {

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
	private @NonNull String author;

	@NotBlank(message = "empty title is not valid")
	private @NonNull String title;

	@ISBN
	private @NonNull String isbn;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "publish_date", nullable = false, unique = false)
	private @NonNull LocalDate publishDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "genre", nullable = false, unique = false)
	private @NonNull Genre genre;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "country", nullable = false, unique = false)
	private @NonNull Country country;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "language", nullable = false, unique = false)
	private @NonNull Language language;

	@Positive
	@Column(name = "page_count", nullable = false, unique = false)
	private int pageCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "size", nullable = false, unique = false)
	private Size size;

	@AttributeOverride(name = "type", column = @Column(name = "cover_type", nullable = false, unique = false))
	@AttributeOverride(name = "surface", column = @Column(name = "cover_surface", nullable = false, unique = false))
	private Cover cover;

}
