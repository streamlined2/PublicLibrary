package com.streamlined.library.service;

import com.streamlined.library.model.dto.ValidationDto;

public interface ValidationService {

	ValidationDto getValidationByClaim(Long claimId, String managerLogin);

	void saveValidation(Long claimId, ValidationDto checkDto, String managerLogin);

}
