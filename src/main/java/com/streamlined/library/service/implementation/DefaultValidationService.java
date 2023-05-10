package com.streamlined.library.service.implementation;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ValidationRepository;
import com.streamlined.library.dao.ClaimRepository;
import com.streamlined.library.model.Validation;
import com.streamlined.library.model.Manager;
import com.streamlined.library.model.dto.ValidationDto;
import com.streamlined.library.model.mapper.ValidationMapper;
import com.streamlined.library.service.MonetaryService;
import com.streamlined.library.service.ValidationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultValidationService implements ValidationService {

	private final ClaimRepository claimRepository;
	private final ValidationRepository validationRepository;
	private final ValidationMapper validationMapper;
	private final MonetaryService monetaryService;
	private final Manager manager;// TODO must be replaced with current authenticated manager from security
									// context

	public ValidationDto getValidationByClaim(Long claimId) {
		var validation = validationRepository.findByClaim(claimId).orElse(getBlankValidation(claimId));
		return validationMapper.toDto(validation);
	}

	private Validation getBlankValidation(Long claimId) {
		var claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new NoEntityFoundException("no claim found with id %d".formatted(claimId)));
		return Validation.builder().claim(claim).manager(manager)
				.compensation(monetaryService.getValue(BigDecimal.ZERO)).build();
	}

	@Transactional
	public void saveValidation(Long claimId, ValidationDto checkDto) {
		var claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new NoEntityFoundException("no claim found with id %d".formatted(claimId)));
		var validation = Validation.builder().id(checkDto.id()).claim(claim).manager(manager)
				.compensation(checkDto.compensation()).build();
		validationRepository.save(validation);
	}

}
