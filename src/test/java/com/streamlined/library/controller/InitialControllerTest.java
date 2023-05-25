package com.streamlined.library.controller;

import static com.streamlined.library.LibraryApplication.DEFAULT_LOCALE;
import static com.streamlined.library.LibraryApplication.LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class InitialControllerTest {

	private MockMvc mvc;

	private InitialController initialController;

	@BeforeEach
	void setUp() throws Exception {
		initialController = new InitialController();
		mvc = MockMvcBuilders.standaloneSetup(initialController).build();
	}

	@Test
	@DisplayName("if no language parameter specified then fall back for default")
	void ifNoLanguageSpecified_fallBackForDefaultLanguage() throws Exception {
		var result = mvc.perform(get("/")).andExpect(status().isOk())
				.andExpectAll(view().name("index"), model().attributeExists("languages"),
						model().attribute("selectedLanguage", DEFAULT_LOCALE.getLanguage()),
						handler().methodName("changeLanguage"), model().hasNoErrors())
				.andReturn();

		assertNull(result.getRequest().getParameter(LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER));
	}

	@Test
	@DisplayName("if language parameter specified then switch language")
	void ifLanguageParameterSpecified_switchLanguage() throws Exception {
		final var selectedLanguage = "uk";

		var result = mvc.perform(get("/?" + LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER + "=" + selectedLanguage))
				.andExpect(status().isOk())
				.andExpectAll(view().name("index"), model().attributeExists("languages"),
						model().attribute("selectedLanguage", selectedLanguage), handler().methodName("changeLanguage"),
						model().hasNoErrors())
				.andReturn();

		assertEquals(result.getRequest().getParameter(LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER), selectedLanguage);

	}

}
