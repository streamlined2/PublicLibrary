package com.streamlined.library.service.implementation;

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
import com.streamlined.library.model.Transfer;
import com.streamlined.library.model.dto.CategoryTimeDataDto;
import com.streamlined.library.service.NotificationService;
import com.streamlined.library.service.TransferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultTransferService extends BaseService implements TransferService {

	private final BookRepository bookRepository;
	private final ApprovalRepository approvalRepository;
	private final TransferRepository transferRepository;
	private final NotificationService notificationService;

	@Transactional
	@Override
	public void saveTransfer(Long approvalId, List<Long> bookIds) {
		var approval = approvalRepository.findById(approvalId)
				.orElseThrow(() -> new NoEntityFoundException("no approval found with id %d".formatted(approvalId)));
		var transfer = Transfer.builder().approval(approval).build();
		bookRepository.findAllById(bookIds).forEach(transfer.getBooks()::add);
		Transfer savedTransfer = transferRepository.save(transfer);
		notificationService.notifyTransferAccomplished(savedTransfer);
	}

	@Override
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
