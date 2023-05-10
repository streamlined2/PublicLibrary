package com.streamlined.library.controller;

import java.util.Map;
import static com.streamlined.library.Utilities.getBookIdList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.service.ApprovalService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/approval")
public class ApprovalController {

	private final ApprovalService approvalService;

	@PostMapping("/save/{requestId}")
	public String approveRequest(@PathVariable Long requestId, @RequestParam Map<String, String> bookIds) {
		approvalService.saveApproval(requestId, getBookIdList(bookIds));
		return "redirect:/request/browse";
	}

	@GetMapping("/browse")
	public String showApprovedRequests(Model model) {
		model.addAttribute("approvedRequests", approvalService.getApprovedRequests());
		return "browse-approvals";
	}

	@GetMapping("/details/{approvalId}")
	public String showApprovalDetails(@PathVariable Long approvalId, Model model) {
		model.addAttribute("approval", approvalService.getApprovalById(approvalId).orElseThrow(
				() -> new NoEntityFoundException("no approved request found with id %d".formatted(approvalId))));
		return "browse-approval-details";
	}

}
