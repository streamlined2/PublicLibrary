package com.streamlined.library.model.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

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
	private CredentialsDto credentials;
	private String firstName;
	private String lastName;
	private @DateTimeFormat(iso = ISO.DATE) LocalDate birthDate;
	private String sex;
	private List<String> contacts = new ArrayList<>();

	@Override
	public String toString() {
		return "%s %s (%s %s) %s".formatted(firstName, lastName, credentials.getLogin(),
				DateTimeFormatter.ISO_LOCAL_DATE.format(birthDate),
				contacts.stream().collect(Collectors.joining(",", "[", "]")));
	}

	public String getName() {
		return firstName + " " + lastName;
	}

	public String getContactList() {
		return contacts.stream().collect(Collectors.joining(", "));
	}

}
