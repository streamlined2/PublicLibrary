package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Approval;
import com.streamlined.library.model.dto.ApprovalDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApprovalMapper implements Mapper<ApprovalDto, Approval> {

	private final BookMapper bookMapper;
	private final RequestMapper requestMapper;
	private final LibrarianMapper librarianMapper;

	public ApprovalDto toDto(Approval entity) {
		var approval = ApprovalDto.builder().id(entity.getId()).request(requestMapper.toDto(entity.getRequest()))
				.librarian(librarianMapper.toDto(entity.getLibrarian())).createdTime(entity.getCreatedTime()).build();
		entity.getBooks().stream().map(bookMapper::toDto).forEach(approval::addBook);
		return approval;
	}

	public Approval toEntity(ApprovalDto dto) {
		var approval = Approval.builder().id(dto.id()).request(requestMapper.toEntity(dto.request()))
				.librarian(librarianMapper.toEntity(dto.librarian())).createdTime(dto.createdTime()).build();
		dto.books().forEach(bookDto -> approval.getBooks().add(bookMapper.toEntity(bookDto)));
		return approval;
	}

}
