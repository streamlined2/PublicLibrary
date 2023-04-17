package com.streamlined.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ApprovalRepository;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.TransferRepository;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Transfer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {

	private final BookRepository bookRepository;
	private final ApprovalRepository approvalRepository;
	private final TransferRepository transferRepository;
	private final Librarian librarian;// TODO should be replaced with authenticated user from security context

	@Transactional
	public void saveTransfer(Long approvalId, List<Long> bookIds) {
		var approvalEntity = approvalRepository.findById(approvalId)
				.orElseThrow(() -> new NoEntityFoundException("no approval found with id %d".formatted(approvalId)));
		var transferEntity = Transfer.builder().approval(approvalEntity).librarian(librarian).build();
		bookRepository.findAllById(bookIds).forEach(transferEntity.getBooks()::add);
		transferRepository.save(transferEntity);
	}

}
