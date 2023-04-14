package com.streamlined.library.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
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

	@PostMapping("/add")
	public String addRequest(@RequestParam Map<String, String> parameterValues) {
		requestService.saveRequest(parameterValues.values().stream().map(Long::valueOf).toList());
		return "redirect:/";
	}

}
