package com.streamlined.library.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "approval")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Approval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "request", unique = true, nullable = false)
	private @NonNull Request request;

	@ManyToOne
	@JoinColumn(name = "librarian", updatable = false, unique = false, nullable = false)
	@CreatedBy
	private Librarian librarian;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_time", nullable = false, unique = false, updatable = false)
	private LocalDateTime createdTime;

	@OneToMany
	@JoinTable(name = "approval_book", joinColumns = { @JoinColumn(name = "approval") }, inverseJoinColumns = {
			@JoinColumn(name = "book") })
	private final Set<Book> books = new HashSet<>();

	public Customer getCustomer() {
		return request.getCustomer();
	}

}
