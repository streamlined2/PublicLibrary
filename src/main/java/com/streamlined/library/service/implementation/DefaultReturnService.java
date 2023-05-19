package com.streamlined.library.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.dao.ReturnRepository;
import com.streamlined.library.dao.TransferRepository;
import com.streamlined.library.model.Return;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReturnDto;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.ReturnMapper;
import com.streamlined.library.service.ReturnService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReturnService implements ReturnService {

	private final BookRepository bookRepository;
	private final TransferRepository transferRepository;
	private final ReturnRepository returnRepository;
	private final CustomerRepository customerRepository;
	private final BookMapper bookMapper;
	private final ReturnMapper returnMapper;

	@Override
	public Stream<BookDto> getCustomerBooks(Long customerId) {
		return transferRepository.getCustomerBooks(customerId).map(bookMapper::toDto).stream();
	}

	@Override
	public Stream<ReturnDto> getReturns() {
		return Streamable.of(returnRepository.findAll()).map(returnMapper::toDto).stream();
	}

	@Override
	public Stream<ReturnDto> getBookReturns(Long customerId) {
		return returnRepository.getReturns(customerId).map(returnMapper::toDto).stream();
	}

	@Override
	public Optional<ReturnDto> getBookReturn(Long returnId) {
		return returnRepository.findById(returnId).map(returnMapper::toDto);
	}

	@Transactional
	@Override
	public void saveReturn(Long customerId, List<Long> bookIds) {
		var customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new NoEntityFoundException("no customer found with id %d".formatted(customerId)));
		var returnEntity = Return.builder().customer(customer).build();
		bookRepository.findAllById(bookIds).forEach(returnEntity.getBooks()::add);
		returnRepository.save(returnEntity);
	}

}