package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.dto.CustomerRequestDataDto;
import com.streamlined.library.model.dto.CustomerReviewDataDto;
import com.streamlined.library.model.dto.CustomerSummaryDataDto;
import com.streamlined.library.model.dto.CustomerTimeDataDto;

public interface CustomerService {

	Stream<CustomerDto> getAllCustomers();

	Optional<CustomerDto> getCustomerById(Long id);

	void save(Long id, CustomerDto customerDto);

	CustomerDto createNewCustomer();

	Optional<CustomerDto> getBookHolder(Long bookId);

	Stream<CustomerSummaryDataDto> getSummaryCustomerData();

	Stream<CustomerRequestDataDto> getCustomerRequestData(Long customerId);

	Stream<CustomerTimeDataDto> getCustomerTimeData(Long customerId);
	
	Stream<CustomerReviewDataDto> getCustomerReviewData(Long customerId);

	List<String> getDateBoundaryRepresentation();

	Stream<String> getAllSexes();
	
	CredentialsDto createNewCredentials();

}
