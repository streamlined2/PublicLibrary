package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.WrongRequestParameterException;
import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ApprovalRepository;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.TransferRepository;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Transfer;
import com.streamlined.library.model.dto.CategoryTimeDataDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService extends BaseService {

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

	public Stream<CategoryTimeDataDto> getCategoryData(Optional<String> category) {
		if (category.isEmpty() || category.stream().allMatch(String::isBlank)) {
			return Stream.empty();
		}
		return switch (category.get()) {
		case "genre" -> transferRepository.getGenreData().stream();
		case "country" -> transferRepository.getCountryData().stream();
		case "language" -> transferRepository.getLanguageData().stream();
		case "publish-year" -> transferRepository.getPublishYearData().stream();
		case "size" -> transferRepository.getSizeData().stream();
		case "cover-type" -> transferRepository.getCoverTypeData().stream();
		case "cover-surface" -> transferRepository.getCoverSurfaceData().stream();
		default ->
			throw new WrongRequestParameterException("passed request parameter %s is incorrect".formatted(category));
		};
	}

}
