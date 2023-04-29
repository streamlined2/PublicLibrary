package com.streamlined.library.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/display-holder/{bookId}")
	public String displayBookHolder(@PathVariable Long bookId, Model model) {
		var holder = customerService.getBookHolder(bookId);
		model.addAttribute("searchResult", holder.isPresent());
		model.addAttribute("name", holder.map(CustomerDto::getName).orElse(""));
		model.addAttribute("contactList", holder.map(CustomerDto::getContactList).orElse(""));
		return "view-book-holder";
	}

	@GetMapping("/get-book-list")
	public String showTakenBooks(Model model) {
		model.addAttribute("customerList", customerService.getAllCustomers());
		return "browse-customers-for-booklist";
	}

	@GetMapping("/display-data")
	public String displayData(Model model) {
		model.addAttribute("age", customerService.getDateBoundaryRepresentation());
		model.addAttribute("data", customerService.getSummaryCustomerData());
		return "display-customer-data";
	}

	@GetMapping("/view-customer-data")
	public String viewData(Model model) {
		model.addAttribute("customerList", customerService.getAllCustomers());
		return "select-customer";
	}

	@GetMapping("/display-customer-request-data/{customerId}")
	public String displayCustomerRequestData(@PathVariable Long customerId, Model model) {
		model.addAttribute("preferences", customerService.getCustomerRequestData(customerId));
		return "view-customer-request-data";
	}

	@GetMapping("/display-customer-time-data/{customerId}")
	public String displayCustomerTimeData(@PathVariable Long customerId, Model model) {
		model.addAttribute("preferences", customerService.getCustomerTimeData(customerId));
		return "view-customer-time-data";
	}
	
	@GetMapping("/display-customer-review-data/{customerId}")
	public String displayCustomerReviewData(@PathVariable Long customerId, Model model) {
		model.addAttribute("preferences", customerService.getCustomerReviewData(customerId));
		return "view-customer-review-data";
	}

}
