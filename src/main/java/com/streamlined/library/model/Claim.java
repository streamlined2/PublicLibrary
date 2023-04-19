package com.streamlined.library.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Currency;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import jakarta.validation.constraints.Positive;
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
@Table(name = "claim", uniqueConstraints = @UniqueConstraint(columnNames = { "customer", "librarian", "book",
		"created_time" }))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(of = "id")
public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "customer", nullable = false, unique = false)
	private @NonNull Customer customer;

	@ManyToOne(optional = false)
	@JoinColumn(name = "librarian", nullable = false, unique = false)
	private @NonNull Librarian librarian;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book", nullable = false, unique = false)
	private @NonNull Book book;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_time", nullable = false, unique = false)
	private LocalDateTime createdTime;

	@NotBlank(message = "damage description should not be blank")
	@Length(min = 15, max = 255)
	@Column(name = "damage_description", nullable = false, unique = false)
	private @NonNull String damageDescription;

	@Currency(value = { "USD" }, message = "damage compensation sum")
	@Positive
	@Column(name = "compensation", nullable = true, unique = false)
	private BigDecimal compensation;

}
