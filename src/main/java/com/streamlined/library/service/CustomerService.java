package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.access.prepost.PreAuthorize;

import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.dto.CustomerRequestDataDto;
import com.streamlined.library.model.dto.CustomerReviewDataDto;
import com.streamlined.library.model.dto.CustomerSummaryDataDto;
import com.streamlined.library.model.dto.CustomerTimeDataDto;
import com.streamlined.library.security.IsLibrarianOrManager;

public interface CustomerService extends UserService {

	Optional<CustomerDto> getCustomerByLogin(String login);

	@IsLibrarianOrManager
	Stream<CustomerDto> getAllCustomers();

	Optional<CustomerDto> getCustomerById(Long id);

	@PreAuthorize("hasAnyRole('LIBRARIAN') or (hasAnyRole('CUSTOMER') and (#customerDto.credentials.login == principal.username))")
	void save(Long id, CustomerDto customerDto);

	CustomerDto createNewCustomer();

	@IsLibrarianOrManager
	Optional<CustomerDto> getBookHolder(Long bookId);

	@IsLibrarianOrManager
	Stream<CustomerSummaryDataDto> getSummaryCustomerData();

	@IsLibrarianOrManager
	Stream<CustomerRequestDataDto> getCustomerRequestData(Long customerId);

	@IsLibrarianOrManager
	Stream<CustomerTimeDataDto> getCustomerTimeData(Long customerId);

	@IsLibrarianOrManager
	Stream<CustomerReviewDataDto> getCustomerReviewData(Long customerId);

	List<String> getDateBoundaryRepresentation();

}
