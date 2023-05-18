package com.streamlined.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.model.dto.UserInterfaceLanguageDto;

import static com.streamlined.library.LibraryApplication.DEFAULT_LANGUAGE;
import static com.streamlined.library.LibraryApplication.LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER;

@Controller
@RequestMapping("/")
public class InitialController {

	@ModelAttribute(name = "languages")
	public List<UserInterfaceLanguageDto> languages() {
		return List.of(new UserInterfaceLanguageDto("en", "English"), new UserInterfaceLanguageDto("uk", "Українська"));
	}

	@GetMapping("/")
	public String changeLanguage(@RequestParam(LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER) Optional<String> language,
			Model model) {
		model.addAttribute("selectedLanguage", language.orElse(DEFAULT_LANGUAGE));
		return "index";
	}

}
