package com.streamlined.library.model.mapper;

import java.nio.CharBuffer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Manager;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.dto.ManagerDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ManagerMapper implements Mapper<ManagerDto, Manager> {

	private final PasswordEncoder passwordEncoder;

	public ManagerDto toDto(Manager entity) {
		return ManagerDto.builder().id(entity.getId()).login(entity.getLogin()).firstName(entity.getFirstName())
				.lastName(entity.getLastName()).birthDate(entity.getBirthDate()).sex(entity.getSex().name())
				.contacts(entity.getContacts().stream().map(Contact::getContactInfo).toList()).build();
	}

	public Manager toEntity(ManagerDto dto) {
		var passwordHash = passwordEncoder.encode(CharBuffer.wrap(dto.getPassword()));
		var manager = Manager.builder().id(dto.getId()).login(dto.getLogin()).firstName(dto.getFirstName())
				.lastName(dto.getLastName()).passwordHash(passwordHash).birthDate(dto.getBirthDate())
				.sex(Sex.valueOf(dto.getSex())).build();
		for (var contactInfo : dto.getContacts()) {
			manager.getContacts().add(Contact.create(contactInfo));
		}
		return manager;
	}

}
