package com.streamlined.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.model.Customer;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

	private final CustomerService customerService;
	private final Customer customer;// TODO should be replaced with authenticated user from security context

	@ModelAttribute(name = "sexList")
	public List<String> sexList() {
		return customerService.getAllSexes().toList();
	}

	@GetMapping("/edit-data")
	public String editPersonalData(Model model) {
		model.addAttribute("user", customerService.getCustomerById(customer.getId()).orElseThrow(
				() -> new NoEntityFoundException("no customer found with id %d".formatted(customer.getId()))));
		return "edit-customer";
	}

	@PostMapping("/edit-data")
	public String savePersonalData(@RequestParam(required = false) Long id, CustomerDto customerDto) {
		customerService.save(id, customerDto);
		return "redirect:/";
	}

	@GetMapping("/register-new")
	public String registerNewCustomer(Model model) {
		model.addAttribute("user", customerService.createNewCustomer());
		return "register-new-customer";
	}

}