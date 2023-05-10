package com.streamlined.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.service.CustomerService;
import com.streamlined.library.service.LibrarianService;
import com.streamlined.library.service.ManagerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final CustomerService customerService;
	private final LibrarianService librarianService;
	private final ManagerService managerService;

	@GetMapping("/checkin")
	public String checkin(Model model) {
		model.addAttribute("credentials", customerService.createNewCredentials());
		log.info("redirecting to login form...");
		return "login";
	}

	@PostMapping("/login")
	public String login(RedirectAttributes attributes, CredentialsDto credentials) {
		attributes.addFlashAttribute("userName", credentials.getLogin());
		log.info("logged in: {}, {}", credentials.getLogin(), String.valueOf(credentials.getPassword()));// TODO remove
																											// sensitive
																											// info
																											// logging
		return "redirect:/";
	}

	@GetMapping("/checkout")
	@PostMapping("/checkout")
	public RedirectView checkout(RedirectAttributes attributes) {
		attributes.getFlashAttributes().remove("userName");// TODO replace with real logout
		log.info("checked out successfully");
		return new RedirectView("/", true);
	}

}
