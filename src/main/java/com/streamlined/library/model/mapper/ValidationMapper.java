package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Validation;
import com.streamlined.library.model.dto.ValidationDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ValidationMapper implements Mapper<ValidationDto, Validation> {

	private final ClaimMapper claimMapper;
	private final ManagerMapper managerMapper;

	@Override
	public Validation toEntity(ValidationDto dto) {
		return Validation.builder().id(dto.id()).claim(claimMapper.toEntity(dto.claim()))
				.manager(managerMapper.toEntity(dto.manager())).createdTime(dto.createdTime())
				.compensation(dto.compensation()).build();
	}

	@Override
	public ValidationDto toDto(Validation entity) {
		return ValidationDto.builder().id(entity.getId()).claim(claimMapper.toDto(entity.getClaim()))
				.manager(managerMapper.toDto(entity.getManager())).createdTime(entity.getCreatedTime())
				.compensation(entity.getCompensation()).build();
	}

}
