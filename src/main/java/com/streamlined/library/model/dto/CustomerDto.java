package com.streamlined.library.model.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import lombok.Builder;

@Builder
public record CustomerDto(Long id, String login, String firstName, String lastName, LocalDate birthDate, String sex,
		List<String> contacts) {

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CustomerDto dto)) {
			return false;
		}
		if (contacts.size() != dto.contacts.size()) {
			return false;
		}
		for (Iterator<String> thisIterator = contacts.iterator(), dtoIterator = dto.contacts.iterator(); thisIterator
				.hasNext();) {
			if (!Objects.equals(thisIterator.next(), dtoIterator.next())) {
				return false;
			}
		}
		return Objects.equals(id, dto.id) && Objects.equals(login, dto.login)
				&& Objects.equals(firstName, dto.firstName) && Objects.equals(lastName, dto.lastName)
				&& Objects.equals(birthDate, dto.birthDate) && Objects.equals(sex, dto.sex);
	}

	@Override
	public int hashCode() {
		int hash = Objects.hash(id, login, firstName, lastName, birthDate, sex);
		for (var contact : contacts) {
			hash = 31 * hash + Objects.hash(contact);
		}
		return hash;
	}

	@Override
	public String toString() {
		StringJoiner join = new StringJoiner(",", "PersonDto [", "]");
		join.add("id=" + Long.toString(id)).add("login=" + login).add("first name=" + firstName)
				.add("last name=" + lastName).add("birth date=" + DateTimeFormatter.ISO_LOCAL_DATE.format(birthDate))
				.add("sex=" + sex);
		for (var contact : contacts) {
			join.add("contact=" + contact);
		}
		return join.toString();
	}

}
