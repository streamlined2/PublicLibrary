package com.streamlined.library.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.model.Person;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.mapper.CustomerMapper;

import static com.streamlined.library.Utilities.toStream;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	public Stream<String> getAllSexes() {
		return Person.Sex.getAllSexes();
	}

	public Stream<CustomerDto> getAllCustomers() {
		return toStream(customerRepository.findAll()).map(customerMapper::toDto);
	}

	public Optional<CustomerDto> getCustomerById(Long id) {
		return customerRepository.findById(id).map(customerMapper::toDto);
	}

	@Transactional
	public void save(Long id, CustomerDto customerDto) {
		var entity = customerMapper.toEntity(customerDto);
		entity.setId(id);
		customerRepository.save(entity);
	}

	public CustomerDto createNewCustomer() {
		return new CustomerDto();
	}

}
