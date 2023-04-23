package com.streamlined.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.service.LibrarianService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/librarian")
public class LibrarianController {

	private final LibrarianService librarianService;
	private final Librarian librarian;// TODO should be replaced with authenticated user from security context

	@ModelAttribute(name = "sexList")
	public List<String> sexList() {
		return librarianService.getAllSexes().toList();
	}

	@GetMapping("/edit-data")
	public String editPersonalData(Model model) {
		model.addAttribute("user", librarianService.getLibrarianById(librarian.getId()).orElseThrow(
				() -> new NoEntityFoundException("no librarian found with id %d".formatted(librarian.getId()))));
		return "edit-librarian";
	}

	@PostMapping("/edit-data")
	public String savePersonalData(@RequestParam(required = false) Long id, LibrarianDto librarianDto) {
		librarianService.save(id, librarianDto);
		return "redirect:/";
	}

	@GetMapping("/register-new")
	public String registerNewLibrarian(Model model) {
		model.addAttribute("user", librarianService.createNewLibrarian());
		return "register-new-librarian";
	}

}
