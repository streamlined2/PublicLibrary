package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.access.prepost.PreAuthorize;

import com.streamlined.library.model.dto.ManagerDto;
import com.streamlined.library.security.IsManager;

@IsManager
public interface ManagerService extends UserService {

	Stream<ManagerDto> getAllManagers();

	Optional<ManagerDto> getManagerById(Long id);

	Optional<ManagerDto> getManagerByLogin(String login);

	@PreAuthorize("#managerDto.credentials.login == principal.username")
	void save(Long id, ManagerDto managerDto);

	ManagerDto createNewManager();

}
