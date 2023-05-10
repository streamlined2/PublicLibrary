package com.streamlined.library.service.implementation;

import java.nio.CharBuffer;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.streamlined.library.model.Person;
import com.streamlined.library.model.dto.CredentialsDto;

public abstract class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Stream<String> getAllSexes() {
		return Person.Sex.getAllSexes();
	}

	public CredentialsDto createNewCredentials() {
		return new CredentialsDto();
	}

	public boolean isValidUser(CredentialsDto credentials, String encodedPassword) {
		return passwordEncoder.matches(CharBuffer.wrap(credentials.getPassword()), encodedPassword);
	}

}