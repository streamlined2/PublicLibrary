package com.streamlined.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.streamlined.library.model.dto.ClaimDto;
import com.streamlined.library.service.ClaimService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/claim")
public class ClaimController {

	private final ClaimService claimService;

	@GetMapping("/return/{returnId}/book/{bookId}")
	public String addClaim(@PathVariable Long returnId, @PathVariable Long bookId, Model model) {
		model.addAttribute("claim", claimService.getExistingOrBlankClaim(returnId, bookId));
		return "add-edit-claim";
	}

	@PostMapping("/return/{returnId}/book/{bookId}")
	public String saveClaim(@PathVariable Long returnId, @PathVariable Long bookId, ClaimDto claimDto, Model model) {
		claimService.saveClaim(returnId, bookId, claimDto);
		return "redirect:/return/submit-claim/" + returnId;
	}

}
