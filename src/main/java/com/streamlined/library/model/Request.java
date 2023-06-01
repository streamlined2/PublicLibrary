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
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "request", uniqueConstraints = @UniqueConstraint(columnNames = { "created_time", "customer" }))
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_time", nullable = false, unique = false, updatable = false)
	private LocalDateTime createdTime;

	@ManyToOne(optional = false)
	@JoinColumn(name = "customer", updatable = false, nullable = false)
	@CreatedBy
	private Customer customer;

	@OneToMany
	@JoinTable(name = "request_book", joinColumns = { @JoinColumn(name = "request") }, inverseJoinColumns = {
			@JoinColumn(name = "book") })
	private final Set<Book> books = new HashSet<>();

	public Customer getCustomer() {
		return customer;
	}

}
