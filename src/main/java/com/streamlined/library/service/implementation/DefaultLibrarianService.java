package com.streamlined.library.service.implementation;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.model.mapper.LibrarianMapper;
import com.streamlined.library.service.LibrarianService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultLibrarianService extends DefaultUserService implements LibrarianService {

	private final LibrarianRepository librarianRepository;
	private final LibrarianMapper librarianMapper;

	@Override
	public Optional<LibrarianDto> getLibrarianByLogin(String userLogin) {
		return librarianRepository.findByLogin(userLogin).map(librarianMapper::toDto);
	}

	@Override
	public Stream<LibrarianDto> getAllLibrarians() {
		return Streamable.of(librarianRepository.findAll()).map(librarianMapper::toDto).stream();
	}

	@Override
	public Optional<LibrarianDto> getLibrarianById(Long id) {
		return librarianRepository.findById(id).map(librarianMapper::toDto);
	}

	@Transactional
	@Override
	public void save(Long id, LibrarianDto librarianDto) {
		var entity = librarianMapper.toEntity(librarianDto);
		entity.setId(id);
		librarianRepository.save(entity);
	}

	@Override
	public LibrarianDto createNewLibrarian() {
		return new LibrarianDto();
	}

}
