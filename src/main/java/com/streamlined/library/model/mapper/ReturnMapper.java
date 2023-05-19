package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Return;
import com.streamlined.library.model.dto.ReturnDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReturnMapper implements Mapper<ReturnDto, Return> {

	private final CustomerMapper customerMapper;
	private final LibrarianMapper librarianMapper;
	private final BookMapper bookMapper;

	@Override
	public Return toEntity(ReturnDto dto) {
		var entity = Return.builder().id(dto.id()).customer(customerMapper.toEntity(dto.customer()))
				.librarian(librarianMapper.toEntity(dto.librarian())).createdTime(dto.createdTime()).build();
		dto.books().stream().map(bookMapper::toEntity).forEach(entity.getBooks()::add);
		return entity;
	}

	@Override
	public ReturnDto toDto(Return entity) {
		var dto = ReturnDto.builder().id(entity.getId()).customer(customerMapper.toDto(entity.getCustomer()))
				.librarian(librarianMapper.toDto(entity.getLibrarian())).createdTime(entity.getCreatedTime()).build();
		entity.getBooks().stream().map(bookMapper::toDto).forEach(dto::addBook);
		return dto;
	}

}
