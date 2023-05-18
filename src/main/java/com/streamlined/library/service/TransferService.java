package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.CategoryDto;
import com.streamlined.library.model.dto.CategoryTimeDataDto;
import com.streamlined.library.security.IsLibrarian;

@IsLibrarian
public interface TransferService {
	
	void saveTransfer(Long approvalId, List<Long> bookIds, String librarianLogin);
	
	Stream<CategoryTimeDataDto> getCategoryData(Optional<String> category);
	
	Stream<CategoryDto> getCategories();

}
