package com.streamlined.library.service.implementation;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ValidationRepository;
import com.streamlined.library.dao.ClaimRepository;
import com.streamlined.library.dao.ManagerRepository;
import com.streamlined.library.model.Validation;
import com.streamlined.library.model.dto.ValidationDto;
import com.streamlined.library.model.mapper.ValidationMapper;
import com.streamlined.library.service.MonetaryService;
import com.streamlined.library.service.ValidationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultValidationService implements ValidationService {

	private final ManagerRepository managerRepository;
	private final ClaimRepository claimRepository;
	private final ValidationRepository validationRepository;
	private final ValidationMapper validationMapper;
	private final MonetaryService monetaryService;

	@Override
	public ValidationDto getValidationByClaim(Long claimId, String managerLogin) {
		var validation = validationRepository.findByClaim(claimId).orElse(getBlankValidation(claimId, managerLogin));
		return validationMapper.toDto(validation);
	}

	private Validation getBlankValidation(Long claimId, String managerLogin) {
		var manager = managerRepository.findByLogin(managerLogin).orElseThrow(
				() -> new NoEntityFoundException("no manager found with login %s".formatted(managerLogin)));
		var claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new NoEntityFoundException("no claim found with id %d".formatted(claimId)));
		return Validation.builder().claim(claim).manager(manager)
				.compensation(monetaryService.getValue(BigDecimal.ZERO)).build();
	}

	@Transactional
	@Override
	public void saveValidation(Long claimId, ValidationDto checkDto, String managerLogin) {
		var manager = managerRepository.findByLogin(managerLogin).orElseThrow(
				() -> new NoEntityFoundException("no manager found with login %s".formatted(managerLogin)));
		var claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new NoEntityFoundException("no claim found with id %d".formatted(claimId)));
		var validation = Validation.builder().id(checkDto.id()).claim(claim).manager(manager)
				.compensation(checkDto.compensation()).build();
		validationRepository.save(validation);
	}

}
