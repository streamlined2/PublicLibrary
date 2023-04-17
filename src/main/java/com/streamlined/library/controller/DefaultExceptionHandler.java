package com.streamlined.library.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

import static com.streamlined.library.Utilities.getSourceURI;

@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

	@ExceptionHandler
	protected ModelAndView handle(Exception ex, WebRequest request) {
		log.error("exception caught by hanlder: ", ex);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("uri", getSourceURI(request).toString());
		parameters.put("context", request.getContextPath());
		parameters.put("message", ex.getMessage());
		return new ModelAndView("error", parameters);
	}

}
