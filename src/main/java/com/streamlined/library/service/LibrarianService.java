package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.model.mapper.LibrarianMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibrarianService extends UserService {

	private final LibrarianRepository librarianRepository;
	private final LibrarianMapper librarianMapper;

	public Stream<LibrarianDto> getAllLibrarians() {
		return Streamable.of(librarianRepository.findAll()).map(librarianMapper::toDto).stream();
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
