package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.access.prepost.PreAuthorize;

import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.security.IsLibrarianOrManager;

@IsLibrarianOrManager
public interface LibrarianService extends UserService {

	Optional<LibrarianDto> getLibrarianByLogin(String userLogin);

	Stream<LibrarianDto> getAllLibrarians();

	Optional<LibrarianDto> getLibrarianById(Long id);

	@PreAuthorize("#librarianDto.credentials.login == principal.username")
	void save(Long id, LibrarianDto librarianDto);

	LibrarianDto createNewLibrarian();

}
