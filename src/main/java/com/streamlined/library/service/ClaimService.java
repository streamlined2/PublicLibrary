package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.ClaimDto;

public interface ClaimService {

	Optional<ClaimDto> getClaim(Long returnId, Long bookId);

	Optional<ClaimDto> getClaim(Long claimId);

	ClaimDto getExistingOrBlankClaim(Long returnId, Long bookId);

	Stream<ClaimDto> getAllClaims();

	void saveClaim(Long returnId, Long bookId, ClaimDto dto, String librarianLogin);

}
