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
import com.streamlined.library.model.dto.CustomerRequestDataDto;
import com.streamlined.library.model.dto.CustomerReviewDataDto;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.dto.CustomerSummaryDataDto;
import com.streamlined.library.model.dto.CustomerTimeDataDto;
import com.streamlined.library.model.mapper.CustomerMapper;
import com.streamlined.library.service.CustomerService;
import com.streamlined.library.service.DateBoundaryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCustomerService extends UserService implements CustomerService {

	private final DateBoundaryService dateBoundaryService;
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final BookRepository bookRepository;

	public Stream<CustomerDto> getAllCustomers() {
		return Streamable.of(customerRepository.findAll()).map(customerMapper::toDto).stream();
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

	public Optional<CustomerDto> getBookHolder(Long bookId) {
		var book = bookRepository.findById(bookId)
				.orElseThrow(() -> new NoEntityFoundException("no book found with id %d".formatted(bookId)));
		return customerRepository.getBookHolders(book).stream().findFirst().map(customerMapper::toDto);
	}

	public Stream<CustomerSummaryDataDto> getSummaryCustomerData() {
		var boundary = dateBoundaryService.getDateBoundary();
		return customerRepository
				.getSummaryCustomerData(boundary.get(0), boundary.get(1), boundary.get(2), boundary.get(3)).stream();
	}

	public Stream<CustomerRequestDataDto> getCustomerRequestData(Long customerId) {
		return customerRepository.getCustomerRequestData(customerId).stream();
	}

	public Stream<CustomerTimeDataDto> getCustomerTimeData(Long customerId) {
		return customerRepository.getCustomerTimeData(customerId).stream();
	}

	public Stream<CustomerReviewDataDto> getCustomerReviewData(Long customerId) {
		return customerRepository.getCustomerReviewData(customerId).stream();
	}

	public List<String> getDateBoundaryRepresentation() {
		return dateBoundaryService.getDateBoundaryRepresentation();
	}

}
