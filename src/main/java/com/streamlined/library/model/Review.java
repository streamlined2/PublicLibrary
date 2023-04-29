package com.streamlined.library.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "review", uniqueConstraints = @UniqueConstraint(columnNames = { "book", "customer" }))
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Review {

	public enum Rating {
		POOR(1), FAIR(2), FINE(3), GOOD(4), EXCELLENT(5);

		private Double grade;

		Rating(int grade) {
			this.grade = Double.valueOf(grade);
		}

		public Double grade() {
			return grade;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book", nullable = false, unique = false)
	private @NotNull Book book;

	@ManyToOne(optional = false)
	@JoinColumn(name = "customer", nullable = false, unique = false)
	private @NotNull Customer customer;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time", nullable = false, unique = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time", nullable = false, unique = false)
	@UpdateTimestamp
	private LocalDateTime updatedTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "rating", nullable = false, unique = false)
	private @NotNull Rating rating;

	@Size(min = 10)
	@NotBlank(message = "review should not be empty")
	@Column(name = "text", nullable = false, unique = false)
	private @NotNull String text;

}
