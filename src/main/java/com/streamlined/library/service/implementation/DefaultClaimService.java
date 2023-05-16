package com.streamlined.library.service.implementation;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.ClaimRepository;
import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.dao.ReturnRepository;
import com.streamlined.library.model.Claim;
import com.streamlined.library.model.dto.ClaimDto;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.ClaimMapper;
import com.streamlined.library.model.mapper.ReturnMapper;
import com.streamlined.library.service.ClaimService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultClaimService implements ClaimService {

	private final LibrarianRepository librarianRepository;
	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	private final ReturnRepository returnRepository;
	private final ReturnMapper returnMapper;
	private final ClaimRepository claimRepository;
	private final ClaimMapper claimMapper;

	@Override
	public Optional<ClaimDto> getClaim(Long returnId, Long bookId) {
		return claimRepository.getClaim(returnId, bookId).map(claimMapper::toDto);
	}

	@Override
	public Optional<ClaimDto> getClaim(Long claimId) {
		return claimRepository.findById(claimId).map(claimMapper::toDto);
	}

	@Override
	public ClaimDto getExistingOrBlankClaim(Long returnId, Long bookId) {
		return getClaim(returnId, bookId).orElse(getBlankClaim(returnId, bookId));
	}

	private ClaimDto getBlankClaim(Long returnId, Long bookId) {
		var bookReturn = returnRepository.findById(returnId)
				.orElseThrow(() -> new NoEntityFoundException("no book return found with id %d".formatted(returnId)));
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with %d".formatted(bookId)));
		return ClaimDto.builder().bookReturn(returnMapper.toDto(bookReturn)).book(bookMapper.toDto(book))
				.damageDescription("").build();
	}

	@Transactional
	@Override
	public void saveClaim(Long returnId, Long bookId, ClaimDto dto, String librarianLogin) {
		var librarian = librarianRepository.findByLogin(librarianLogin).orElseThrow(
				() -> new NoEntityFoundException("no librarian found with login %s".formatted(librarianLogin)));
		var bookReturn = returnRepository.findById(returnId)
				.orElseThrow(() -> new NoEntityFoundException("no return found with id %d".formatted(returnId)));
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with id %d".formatted(bookId)));
		var claim = Claim.builder().id(dto.id()).bookReturn(bookReturn).book(book).librarian(librarian)
				.damageDescription(dto.damageDescription()).build();
		claimRepository.save(claim);
	}

	@Override
	public Stream<ClaimDto> getAllClaims() {
		return claimRepository.getAllClaims().map(claimMapper::toDto).stream();
	}

}
