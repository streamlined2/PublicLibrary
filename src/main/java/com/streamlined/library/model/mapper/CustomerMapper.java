package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.dto.CustomerDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

	public CustomerDto toDto(Customer person) {
		return CustomerDto.builder().id(person.getId()).login(person.getLogin()).firstName(person.getFirstName())
				.lastName(person.getLastName()).birthDate(person.getBirthDate()).sex(person.getSex().name())
				.contacts(person.getContacts().stream().map(Contact::getContactInfo).toList()).build();
	}

	public Customer toEntity(CustomerDto dto) {
		var customer = Customer.builder().id(dto.id()).login(dto.login()).firstName(dto.firstName())
				.lastName(dto.lastName()).birthDate(dto.birthDate()).sex(Sex.valueOf(dto.sex())).build();
		for (var contactInfo : dto.contacts()) {
			customer.getContacts().add(Contact.create(contactInfo));
		}
		return customer;
	}
	
}
