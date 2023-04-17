package com.streamlined.library.controller;

import static com.streamlined.library.Utilities.getBookIdList;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.service.RequestService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

	private final RequestService requestService;

	@GetMapping("/browse")
	public String browseActiveRequests(Model model) {
		model.addAttribute("requestList", requestService.getActiveRequests().toList());
		return "browse-requests";
	}

	@GetMapping("/details/{id}")
	public String browseRequestDetails(@PathVariable Long id, Model model) {
		model.addAttribute("request", requestService.getRequestById(id).orElseThrow(
				() -> new NoEntityFoundException("entity for request with id %d not found".formatted(id))));
		return "browse-request-details";
	}

	@PostMapping("/add")
	public String addRequest(@RequestParam Map<String, String> bookIds) {
		requestService.saveRequest(getBookIdList(bookIds));
		return "redirect:/";
	}

}
