package com.streamlined.library.model.mapper;

import java.nio.CharBuffer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.model.dto.CustomerDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerMapper implements Mapper<CustomerDto, Customer> {

	private final PasswordEncoder passwordEncoder;

	public CustomerDto toDto(Customer entity) {
		return CustomerDto.builder().id(entity.getId()).credentials(new CredentialsDto(entity.getLogin()))
				.firstName(entity.getFirstName()).lastName(entity.getLastName()).birthDate(entity.getBirthDate())
				.sex(entity.getSex().name())
				.contacts(entity.getContacts().stream().map(Contact::getContactInfo).toList()).build();
	}

	public Customer toEntity(CustomerDto dto) {
		var passwordHash = passwordEncoder.encode(CharBuffer.wrap(dto.getCredentials().getPassword()));
		var customer = Customer.builder().id(dto.getId()).login(dto.getCredentials().getLogin())
				.firstName(dto.getFirstName()).lastName(dto.getLastName()).passwordHash(passwordHash)
				.birthDate(dto.getBirthDate()).sex(Sex.valueOf(dto.getSex())).build();
		for (var contactInfo : dto.getContacts()) {
			customer.getContacts().add(Contact.create(contactInfo));
		}
		return customer;
	}

}
