package com.streamlined.library.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.streamlined.library.model.dto.ApprovalDto;

public interface ApprovalService {

	Stream<ApprovalDto> getApprovedRequests();

	Optional<ApprovalDto> getApprovalById(Long id);

	void saveApproval(Long requestId, List<Long> bookIds, String librarianLogin);

}
