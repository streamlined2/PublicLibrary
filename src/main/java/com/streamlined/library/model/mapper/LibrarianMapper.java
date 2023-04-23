package com.streamlined.library.model.mapper;

import java.nio.CharBuffer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.dto.LibrarianDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LibrarianMapper implements Mapper<LibrarianDto, Librarian> {

	private final PasswordEncoder passwordEncoder;

	public LibrarianDto toDto(Librarian entity) {
		return LibrarianDto.builder().id(entity.getId()).login(entity.getLogin()).firstName(entity.getFirstName())
				.lastName(entity.getLastName()).birthDate(entity.getBirthDate()).sex(entity.getSex().name())
				.contacts(entity.getContacts().stream().map(Contact::getContactInfo).toList()).build();
	}

	public Librarian toEntity(LibrarianDto dto) {
		var passwordHash = passwordEncoder.encode(CharBuffer.wrap(dto.getPassword()));
		var librarian = Librarian.builder().id(dto.getId()).login(dto.getLogin()).firstName(dto.getFirstName())
				.lastName(dto.getLastName()).passwordHash(passwordHash).birthDate(dto.getBirthDate())
				.sex(Sex.valueOf(dto.getSex())).build();
		for (var contactInfo : dto.getContacts()) {
			librarian.getContacts().add(Contact.create(contactInfo));
		}
		return librarian;
	}

}
