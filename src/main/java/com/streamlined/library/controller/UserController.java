package com.streamlined.library.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final UserService userService;

	private UserController(@Qualifier("defaultCustomerService") UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/checkin")
	@PostMapping("/checkin")
	public String checkin(@RequestParam Optional<String> error, Model model) {
		model.addAttribute("credentials", userService.createNewCredentials());
		model.addAttribute("error", error.orElse("false"));
		log.debug("redirecting to login form...");
		return "login";
	}

	@PostMapping("/do-login")
	public String login(CredentialsDto credentials) {
		log.debug("logged in as {}", credentials.getLogin());
		return "redirect:/";
	}

}
