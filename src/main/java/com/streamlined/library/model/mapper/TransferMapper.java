package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Transfer;
import com.streamlined.library.model.dto.TransferDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransferMapper implements Mapper<TransferDto, Transfer> {

	private final BookMapper bookMapper;
	private final ApprovalMapper approvalMapper;
	private final LibrarianMapper librarianMapper;

	@Override
	public Transfer toEntity(TransferDto dto) {
		var transfer = Transfer.builder().id(dto.id()).approval(approvalMapper.toEntity(dto.approval()))
				.librarian(librarianMapper.toEntity(dto.librarian())).createdTime(dto.createdTime()).build();
		dto.books().forEach(bookDto -> transfer.getBooks().add(bookMapper.toEntity(bookDto)));
		return transfer;
	}

	@Override
	public TransferDto toDto(Transfer entity) {
		var transfer = TransferDto.builder().id(entity.getId()).approval(approvalMapper.toDto(entity.getApproval()))
				.librarian(librarianMapper.toDto(entity.getLibrarian())).createdTime(entity.getCreatedTime()).build();
		entity.getBooks().stream().map(bookMapper::toDto).forEach(transfer::addBook);
		return transfer;
	}

}
