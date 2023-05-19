package com.streamlined.library.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.streamlined.library.model.dto.ValidationDto;
import com.streamlined.library.service.ValidationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/validation")
public class ValidationController {

	private final ValidationService validationService;

	@GetMapping("/add-edit/{claimId}")
	public String showClaimDetails(@PathVariable Long claimId, Model model, Principal principal) {
		model.addAttribute("validation", validationService.getValidationByClaim(claimId, principal.getName()));
		return "add-edit-check";
	}

	@PostMapping("/add-edit/{claimId}")
	public String saveCheck(@PathVariable Long claimId, ValidationDto checkDto) {
		validationService.saveValidation(claimId, checkDto);
		return "redirect:/";
	}

}
