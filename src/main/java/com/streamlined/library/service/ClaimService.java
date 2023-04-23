package com.streamlined.library.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.ClaimRepository;
import com.streamlined.library.dao.ReturnRepository;
import com.streamlined.library.model.Claim;
import com.streamlined.library.model.dto.ClaimDto;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.ClaimMapper;
import com.streamlined.library.model.mapper.ReturnMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ClaimService {

	private final MonetaryService monetaryService;
	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	private final ReturnRepository returnRepository;
	private final ReturnMapper returnMapper;
	private final ClaimRepository claimRepository;
	private final ClaimMapper claimMapper;

	public Optional<ClaimDto> getClaim(Long returnId, Long bookId) {
		return claimRepository.getClaim(returnId, bookId).map(claimMapper::toDto);
	}

	public ClaimDto getExistingOrBlankClaim(Long returnId, Long bookId) {
		return getClaim(returnId, bookId).orElse(getBlankClaim(returnId, bookId));
	}

	private ClaimDto getBlankClaim(Long returnId, Long bookId) {
		var bookReturn = returnRepository.findById(returnId)
				.orElseThrow(() -> new NoEntityFoundException("no book return found with id %d".formatted(returnId)));
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with %d".formatted(bookId)));
		return ClaimDto.builder().bookReturn(returnMapper.toDto(bookReturn)).book(bookMapper.toDto(book))
				.damageDescription("").compensation(monetaryService.getValue(BigDecimal.ZERO)).build();
	}

	@Transactional
	public void saveClaim(Long returnId, Long bookId, ClaimDto dto) {
		var bookReturn = returnRepository.findById(returnId)
				.orElseThrow(() -> new NoEntityFoundException("no return found with id %d".formatted(returnId)));
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with id %d".formatted(bookId)));
		var claim = Claim.builder().id(dto.id()).bookReturn(bookReturn).book(book)
				.damageDescription(dto.damageDescription()).compensation(dto.compensation()).build();
		claimRepository.save(claim);
	}

}