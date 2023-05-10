package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.ManagerDto;

public interface ManagerService {

	Stream<ManagerDto> getAllManagers();

	Optional<ManagerDto> getManagerById(Long id);

	void save(Long id, ManagerDto managerDto);

	ManagerDto createNewManager();

}
