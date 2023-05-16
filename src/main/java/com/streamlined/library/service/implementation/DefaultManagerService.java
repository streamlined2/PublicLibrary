package com.streamlined.library.service.implementation;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.ManagerRepository;
import com.streamlined.library.model.dto.ManagerDto;
import com.streamlined.library.model.mapper.ManagerMapper;
import com.streamlined.library.service.ManagerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultManagerService extends UserService implements ManagerService {

	private final ManagerRepository managerRepository;
	private final ManagerMapper managerMapper;

	@Override
	public Stream<ManagerDto> getAllManagers() {
		return Streamable.of(managerRepository.findAll()).map(managerMapper::toDto).stream();
	}

	@Override
	public Optional<ManagerDto> getManagerById(Long id) {
		return managerRepository.findById(id).map(managerMapper::toDto);
	}

	@Override
	public Optional<ManagerDto> getManagerByLogin(String login) {
		return managerRepository.findByLogin(login).map(managerMapper::toDto);
	}

	@Transactional
	@Override
	public void save(Long id, ManagerDto managerDto) {
		var entity = managerMapper.toEntity(managerDto);
		entity.setId(id);
		managerRepository.save(entity);
	}

	@Override
	public ManagerDto createNewManager() {
		return new ManagerDto();
	}

}
