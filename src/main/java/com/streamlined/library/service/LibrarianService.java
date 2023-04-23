package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.model.Person;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.model.mapper.LibrarianMapper;

import static com.streamlined.library.Utilities.toStream;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibrarianService {

	private final LibrarianRepository librarianRepository;
	private final LibrarianMapper librarianMapper;

	public Stream<String> getAllSexes() {
		return Person.Sex.getAllSexes();
	}

	public Stream<LibrarianDto> getAllLibrarians() {
		return toStream(librarianRepository.findAll()).map(librarianMapper::toDto);
	}

	public Optional<LibrarianDto> getLibrarianById(Long id) {
		return librarianRepository.findById(id).map(librarianMapper::toDto);
	}

	@Transactional
	public void save(Long id, LibrarianDto librarianDto) {
		var entity = librarianMapper.toEntity(librarianDto);
		entity.setId(id);
		librarianRepository.save(entity);
	}

	public LibrarianDto createNewLibrarian() {
		return new LibrarianDto();
	}

}
