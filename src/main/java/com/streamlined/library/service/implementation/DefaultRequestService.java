package com.streamlined.library.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.WrongRequestParameterException;
import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.dao.RequestRepository;
import com.streamlined.library.model.Request;
import com.streamlined.library.model.dto.CategoryRequestDataDto;
import com.streamlined.library.model.dto.RequestDto;
import com.streamlined.library.model.mapper.RequestMapper;
import com.streamlined.library.service.RequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultRequestService extends BaseService implements RequestService {

	private final CustomerRepository customerRepository;
	private final BookRepository bookRepository;
	private final RequestRepository requestRepository;
	private final RequestMapper requestMapper;

	@Override
	public Stream<RequestDto> getActiveRequests() {
		return requestRepository.findActiveRequests().map(requestMapper::toDto).stream();
	}

	@Override
	public Optional<RequestDto> getRequestById(Long id) {
		return requestRepository.findById(id).map(requestMapper::toDto);
	}

	@Transactional
	@Override
	public void saveRequest(List<Long> bookIdList, String customerLogin) {
		var customer = customerRepository.findByLogin(customerLogin).orElseThrow(
				() -> new NoEntityFoundException("no customer found with login %s".formatted(customerLogin)));
		Request request = Request.builder().customer(customer).build();
		bookRepository.findAllById(bookIdList).forEach(request.getBooks()::add);
		requestRepository.save(request);
	}

	@Override
	public Stream<CategoryRequestDataDto> getCategoryData(Optional<String> category) {
		if (category.isEmpty() || category.stream().allMatch(String::isBlank)) {
			return Stream.empty();
		}
		return switch (category.get()) {
		case "genre" -> requestRepository.getGenreData().stream();
		case "country" -> requestRepository.getCountryData().stream();
		case "language" -> requestRepository.getLanguageData().stream();
		case "publish-year" -> requestRepository.getPublishYearData().stream();
		case "size" -> requestRepository.getSizeData().stream();
		case "cover-type" -> requestRepository.getCoverTypeData().stream();
		case "cover-surface" -> requestRepository.getCoverSurfaceData().stream();
		default ->
			throw new WrongRequestParameterException("passed request parameter %s is incorrect".formatted(category));
		};
	}

}
