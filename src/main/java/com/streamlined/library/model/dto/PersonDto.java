package com.streamlined.library.model.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class PersonDto {

	private Long id;
	private String login;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private String sex;
	private List<String> contacts;

	@Override
	public String toString() {
		return "%s %s (%s %s) %s".formatted(firstName, lastName, login,
				DateTimeFormatter.ISO_LOCAL_DATE.format(birthDate),
				contacts.stream().collect(Collectors.joining(",", "[", "]")));
	}

	public String getName() {
		return firstName + " " + lastName;
	}

}
