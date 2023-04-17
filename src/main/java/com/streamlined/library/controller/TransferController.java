package com.streamlined.library.controller;

import static com.streamlined.library.Utilities.getBookIdList;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.streamlined.library.service.TransferService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {

	private final TransferService transferService;

	@PostMapping("/save/{approvalId}")
	public String createTransfer(@PathVariable Long approvalId, @RequestParam Map<String, String> bookIds) {
		transferService.saveTransfer(approvalId, getBookIdList(bookIds));
		return "redirect:/approval/browse";
	}

}
