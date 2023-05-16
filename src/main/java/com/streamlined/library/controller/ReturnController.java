package com.streamlined.library.controller;

import java.security.Principal;
import java.util.Map;
import static com.streamlined.library.Utilities.getBookIdList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.service.CustomerService;
import com.streamlined.library.service.ReturnService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/return")
public class ReturnController {

	private final CustomerService customerService;
	private final ReturnService returnService;

	@GetMapping("/select-customer")
	public String selectCustomer(Model model) {
		model.addAttribute("customerList", customerService.getAllCustomers());
		return "browse-customers";
	}

	@GetMapping("/customer-books/{customerId}")
	public String showCustomerBooks(@PathVariable Long customerId, Model model) {
		model.addAttribute("customerId", customerId);
		model.addAttribute("customerBooks", returnService.getCustomerBooks(customerId));
		return "browse-customer-books";
	}

	@PostMapping("/customer-books/{customerId}")
	public String saveBookReturn(@PathVariable Long customerId, @RequestParam Map<String, String> bookIds, Model model,
			Principal principal) {
		returnService.saveReturn(customerId, getBookIdList(bookIds), principal.getName());
		return "redirect:/return/select-customer";
	}

	@GetMapping("/customer-returns/{customerId}")
	public String viewReturns(@PathVariable Long customerId, Model model) {
		model.addAttribute("returnList", returnService.getBookReturns(customerId));
		return "browse-book-returns";
	}

	@GetMapping("/submit-claim/{returnId}")
	public String submitClaim(@PathVariable Long returnId, Model model) {
		model.addAttribute("return", returnService.getBookReturn(returnId)
				.orElseThrow(() -> new NoEntityFoundException("no return found with id %d".formatted(returnId))));
		return "return-details";
	}

}
