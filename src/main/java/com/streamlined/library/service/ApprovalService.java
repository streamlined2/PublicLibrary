package com.streamlined.library.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ApprovalRepository;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.RequestRepository;
import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Librarian;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApprovalService {

	private final BookRepository bookRepository;
	private final RequestRepository requestRepository;
	private final ApprovalRepository approvalRepository;
	private final Librarian librarian;//TODO replace with current authenticated user from security context

	@Transactional
	public void saveApproval(Long requestId, List<Long> bookIds) {
		var request = requestRepository.findById(requestId)
				.orElseThrow(() -> new NoEntityFoundException("no request with id %d found".formatted(requestId)));
		var approval = Approval.builder().request(request).librarian(librarian).build();
		bookRepository.findAllById(bookIds).forEach(approval.getBooks()::add);
		approvalRepository.save(approval);
	}

}
