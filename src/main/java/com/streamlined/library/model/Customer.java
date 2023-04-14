package com.streamlined.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@SuperBuilder
@NoArgsConstructor
public class Customer extends Person {

	//TODO remove @Builder implementation for class constructor to support inheritance
	/*
	@Builder
	public Customer(Long id, String login, String passwordHash, String firstName, String lastName, LocalDate birthDate,
			Sex sex, Set<Contact> contacts) {
		super(id, login, passwordHash, firstName, lastName, birthDate, sex);
	}*/

}
