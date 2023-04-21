package com.streamlined.library.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "return", uniqueConstraints = @UniqueConstraint(columnNames = { "customer", "librarian",
		"created_time" }))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode(of = "id")
public class Return {

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

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_time", nullable = false, unique = false, updatable = false)
	private LocalDateTime createdTime;

	@OneToMany
	@JoinTable(name = "return_book", joinColumns = {
			@JoinColumn(name = "return", nullable = false, unique = false) }, inverseJoinColumns = {
					@JoinColumn(name = "book", nullable = false, unique = false) })
	private final Set<Book> books = new HashSet<>();

}
