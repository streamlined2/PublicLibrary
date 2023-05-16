package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.CategoryDto;
import com.streamlined.library.model.dto.CategoryRequestDataDto;
import com.streamlined.library.model.dto.RequestDto;

public interface RequestService {

	Stream<RequestDto> getActiveRequests();

	Optional<RequestDto> getRequestById(Long id);

	void saveRequest(List<Long> bookIdList, String customerLogin);

	Stream<CategoryRequestDataDto> getCategoryData(Optional<String> category);

	Stream<CategoryDto> getCategories();

}
