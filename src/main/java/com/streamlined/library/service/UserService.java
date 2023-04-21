package com.streamlined.library.service;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.mapper.CustomerMapper;

import static com.streamlined.library.Utilities.toStream;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	public Stream<CustomerDto> getAllCustomers() {
		return toStream(customerRepository.findAll()).map(customerMapper::toDto);
	}

}
