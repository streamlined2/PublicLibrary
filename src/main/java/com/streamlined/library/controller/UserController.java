package com.streamlined.library.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@PostMapping("/checkin")
	public String checkin(@RequestParam Optional<String> error, Model model) {
		model.addAttribute("credentials", customerService.createNewCredentials());
		model.addAttribute("error", error.orElse("false"));
		log.info("redirecting to login form...");
		return "login";
	}

	@PostMapping("/do-login")
	public String login(CredentialsDto credentials) {
		log.info("logged in as {}", credentials.getLogin());
		return "redirect:/";
	}

}
