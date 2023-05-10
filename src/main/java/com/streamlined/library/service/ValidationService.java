package com.streamlined.library.service;

import com.streamlined.library.model.dto.ValidationDto;

public interface ValidationService {

	ValidationDto getValidationByClaim(Long claimId);

	void saveValidation(Long claimId, ValidationDto checkDto);

}
