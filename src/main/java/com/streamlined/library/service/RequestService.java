package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.CategoryDto;
import com.streamlined.library.model.dto.CategoryRequestDataDto;
import com.streamlined.library.model.dto.RequestDto;
import com.streamlined.library.security.IsCustomer;
import com.streamlined.library.security.IsCustomerOrLibrarian;
import com.streamlined.library.security.IsLibrarian;

@IsCustomerOrLibrarian
public interface RequestService {

	@IsLibrarian
	Stream<RequestDto> getActiveRequests();

	Optional<RequestDto> getRequestById(Long id);

	@IsCustomer
	void saveRequest(List<Long> bookIdList);

	@IsLibrarian
	Stream<CategoryRequestDataDto> getCategoryData(Optional<String> category);

	Stream<CategoryDto> getCategories();

}
