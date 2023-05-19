package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ReturnDto;
import com.streamlined.library.security.IsLibrarian;

@IsLibrarian
public interface ReturnService {

	Stream<BookDto> getCustomerBooks(Long customerId);

	Stream<ReturnDto> getReturns();

	Stream<ReturnDto> getBookReturns(Long customerId);

	Optional<ReturnDto> getBookReturn(Long returnId);

	void saveReturn(Long customerId, List<Long> bookIds);

}
