package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.LibrarianDto;

public interface LibrarianService {

	Optional<LibrarianDto> getLibrarianByLogin(String userLogin);

	Stream<LibrarianDto> getAllLibrarians();

	Optional<LibrarianDto> getLibrarianById(Long id);

	void save(Long id, LibrarianDto librarianDto);

	LibrarianDto createNewLibrarian();

	Stream<String> getAllSexes();

}
