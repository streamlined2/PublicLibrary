package com.streamlined.library.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "first_name", "last_name", "birth_date" }))
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@SequenceGenerator(name = "personId")
public abstract class Person {

	public enum Sex {
		FEMALE, MALE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personId")
	@Setter(AccessLevel.PROTECTED)
	private Long id;

	@NotBlank(message = "blank login is not valid")
	@Size(min = 10)
	@NaturalId(mutable = true)
	@Column(name = "login", nullable = false, unique = true)
	private @NonNull String login;

	@NotBlank(message = "password can not be blank")
	@Size(min = 10)
	@Column(name = "password_hash", nullable = false, unique = false)
	private @NonNull String passwordHash;

	@NotBlank(message = "first name should not be blank")
	@Column(name = "first_name", nullable = false, unique = false)
	private @NonNull String firstName;

	@NotBlank(message = "last name should not be blank")
	@Column(name = "last_name", nullable = false, unique = false)
	private @NonNull String lastName;

	@Temporal(TemporalType.DATE)
	@Column(name = "birth_date", nullable = false, unique = false)
	private @NonNull LocalDate birthDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "sex", nullable = false, unique = false)
	private @NonNull Sex sex;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumn(name = "person", nullable = false, unique = true)
	private final Set<Contact> contacts = new HashSet<>();

}
