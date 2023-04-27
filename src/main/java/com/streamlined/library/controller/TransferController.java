package com.streamlined.library.controller;

import static com.streamlined.library.Utilities.getBookIdList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.model.dto.CategoryDto;
import com.streamlined.library.service.TransferService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

	private final TransferService transferService;

	@ModelAttribute(name = "categoryList")
	public List<CategoryDto> categoryList() {
		return transferService.getCategories().toList();
	}
	
	@PostMapping("/save/{approvalId}")
	public String createTransfer(@PathVariable Long approvalId, @RequestParam Map<String, String> bookIds) {
		transferService.saveTransfer(approvalId, getBookIdList(bookIds));
		return "redirect:/approval/browse";
	}

	@GetMapping("/most-popular-books")
	public String fetchMostPopularBooks(@RequestParam Optional<String> category, Model model) {
		model.addAttribute("categoryName", category.orElse(""));
		model.addAttribute("categoryData", transferService.getCategoryData(category).toList());
		return "browse-category-by-time";
	}

}
