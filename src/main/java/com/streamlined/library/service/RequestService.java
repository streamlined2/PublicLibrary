package com.streamlined.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.RequestRepository;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Request;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestService {

	private final BookRepository bookRepository;
	private final RequestRepository requestRepository;
	private final Customer customer;// TODO must be replaced with authenticated user from security context

	@Transactional
	public void saveRequest(List<Long> bookIdList) {
		Request request = Request.builder().customer(customer).build();
		bookRepository.findAllById(bookIdList).forEach(request.getBooks()::add);
		requestRepository.save(request);
	}

}
