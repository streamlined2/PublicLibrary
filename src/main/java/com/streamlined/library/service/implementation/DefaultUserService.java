package com.streamlined.library.service.implementation;

import java.util.stream.Stream;

import com.streamlined.library.model.Person;
import com.streamlined.library.model.dto.CredentialsDto;

public abstract class DefaultUserService {

	public Stream<String> getAllSexes() {
		return Person.Sex.getAllSexes();
	}

	public CredentialsDto createNewCredentials() {
		return new CredentialsDto();
	}

}
