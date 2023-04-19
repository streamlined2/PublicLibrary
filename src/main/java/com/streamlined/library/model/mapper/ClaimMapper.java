package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Claim;
import com.streamlined.library.model.dto.ClaimDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClaimMapper implements Mapper<ClaimDto, Claim> {

	private final CustomerMapper customerMapper;
	private final LibrarianMapper librarianMapper;
	private final BookMapper bookMapper;

	@Override
	public Claim toEntity(ClaimDto dto) {
		return Claim.builder().id(dto.id()).customer(customerMapper.toEntity(dto.customer()))
				.librarian(librarianMapper.toEntity(dto.librarian())).book(bookMapper.toEntity(dto.book()))
				.createdTime(dto.createdTime()).damageDescription(dto.damageDescription())
				.compensation(dto.compensation()).build();
	}

	@Override
	public ClaimDto toDto(Claim entity) {
		return ClaimDto.builder().id(entity.getId()).customer(customerMapper.toDto(entity.getCustomer()))
				.librarian(librarianMapper.toDto(entity.getLibrarian())).book(bookMapper.toDto(entity.getBook()))
				.createdTime(entity.getCreatedTime()).damageDescription(entity.getDamageDescription())
				.compensation(entity.getCompensation()).build();
	}

}
