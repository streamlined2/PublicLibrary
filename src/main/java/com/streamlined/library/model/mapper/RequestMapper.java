package com.streamlined.library.model.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Request;
import com.streamlined.library.model.dto.RequestDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestMapper implements Mapper<RequestDto, Request> {

	private final CustomerMapper customerMapper;
	private final BookMapper bookMapper;

	public RequestDto toDto(Request entity) {
		return RequestDto.builder().id(entity.getId()).createdTime(entity.getCreatedTime())
				.customer(customerMapper.toDto(entity.getCustomer()))
				.books(entity.getBooks().stream().map(bookMapper::toDto).collect(Collectors.toSet())).build();
	}

	public Request toEntity(RequestDto dto) {
		var request = Request.builder().id(dto.id()).createdTime(dto.createdTime())
				.customer(customerMapper.toEntity(dto.customer())).build();
		dto.books().forEach(bookDto -> request.getBooks().add(bookMapper.toEntity(bookDto)));
		return request;
	}

}
