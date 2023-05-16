package com.streamlined.library.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.model.dto.ManagerDto;
import com.streamlined.library.service.ManagerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

	private final ManagerService managerService;

	@ModelAttribute(name = "sexList")
	public List<String> sexList() {
		return managerService.getAllSexes().toList();
	}

	@GetMapping("/edit-data")
	public String editPersonalData(Model model, Principal principal) {
		var userLogin = principal.getName();
		model.addAttribute("user", managerService.getManagerByLogin(userLogin).orElseThrow(
				() -> new NoEntityFoundException("no manager found with login %s".formatted(userLogin))));
		return "edit-manager";
	}

	@PostMapping("/edit-data")
	public String savePersonalData(@RequestParam(required = false) Long id, ManagerDto managerDto) {
		managerService.save(id, managerDto);
		return "redirect:/";
	}

	@GetMapping("/register-new")
	public String registerNewManager(Model model) {
		model.addAttribute("user", managerService.createNewManager());
		return "register-new-manager";
	}

}
