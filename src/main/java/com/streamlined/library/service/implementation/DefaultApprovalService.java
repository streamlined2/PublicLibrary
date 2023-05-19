package com.streamlined.library.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ApprovalRepository;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.RequestRepository;
import com.streamlined.library.model.Approval;
import com.streamlined.library.model.dto.ApprovalDto;
import com.streamlined.library.model.mapper.ApprovalMapper;
import com.streamlined.library.service.ApprovalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultApprovalService implements ApprovalService {

	private final BookRepository bookRepository;
	private final RequestRepository requestRepository;
	private final ApprovalRepository approvalRepository;
	private final ApprovalMapper approvalMapper;

	@Override
	public Stream<ApprovalDto> getApprovedRequests() {
		return approvalRepository.getApprovedRequests().map(approvalMapper::toDto).stream();
	}

	@Override
	public Optional<ApprovalDto> getApprovalById(Long id) {
		return approvalRepository.findById(id).map(approvalMapper::toDto);
	}

	@Transactional
	@Override
	public void saveApproval(Long requestId, List<Long> bookIds) {
		var request = requestRepository.findById(requestId)
				.orElseThrow(() -> new NoEntityFoundException("no request with id %d found".formatted(requestId)));
		var approval = Approval.builder().request(request).build();
		bookRepository.findAllById(bookIds).forEach(approval.getBooks()::add);
		approvalRepository.save(approval);
	}

}
