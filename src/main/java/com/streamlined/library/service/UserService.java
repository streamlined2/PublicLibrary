package com.streamlined.library.service;

import java.util.stream.Stream;

import com.streamlined.library.model.dto.CredentialsDto;

public interface UserService {

	Stream<String> getAllSexes();
	
	CredentialsDto createNewCredentials();

}
