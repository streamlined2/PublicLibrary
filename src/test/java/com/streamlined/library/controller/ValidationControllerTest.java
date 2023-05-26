package com.streamlined.library.controller;

import static com.streamlined.library.Utilities.listOf;
import static com.streamlined.library.Utilities.sortedSetOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.ClaimDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.model.dto.ManagerDto;
import com.streamlined.library.model.dto.ReturnDto;
import com.streamlined.library.model.dto.ValidationDto;
import com.streamlined.library.service.MonetaryService;
import com.streamlined.library.service.ValidationService;
import com.streamlined.library.service.implementation.DefaultMonetaryService;

class ValidationControllerTest {

	private static final String CURRENCY_CODE = "USD";

	private MockMvc mvc;
	private AutoCloseable resource;

	private ValidationController validationController;

	private MonetaryService monetaryService;

	@Mock
	private ValidationService validationService;

	@Mock
	private Principal principal;

	@BeforeEach
	void setUp() throws Exception {
		resource = MockitoAnnotations.openMocks(this);
		validationController = new ValidationController(validationService);
		monetaryService = new DefaultMonetaryService(CURRENCY_CODE);
		mvc = MockMvcBuilders.standaloneSetup(validationController).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		resource.close();
	}

	@Test
	@DisplayName("should show claim details for given claim id")
	void shouldShowClaimDetailsForGivenId() throws Exception {

		final var claimId = 1L;
		final var managerLogin = "manager";
		final double sum = 300.00;
		final var userName = "name";

		final var validationDto = createValidationDto(createClaimDto(claimId), createManagerDto(managerLogin), sum);

		when(validationService.getValidationByClaim(anyLong(), anyString())).thenReturn(validationDto);
		when(principal.getName()).thenReturn(userName);

		var result = mvc.perform(get("/validation/add-edit/{claimId}", claimId).principal(principal))
				.andExpectAll(status().isOk(), view().name("add-edit-check"),
						model().attribute("validation", validationDto), handler().methodName("showClaimDetails"),
						model().hasNoErrors(), request().attribute("claimId", claimId))
				.andReturn();

		assertEquals(principal, result.getRequest().getUserPrincipal());

	}

	@Test
	@DisplayName("should save validation entity for given claim id, validation DTO")
	void shouldSaveValidationForClaimIdAndDTO() throws Exception {
		final var claimId = 1L;

		var result = mvc.perform(post("/validation/add-edit/{claimId}", claimId).principal(principal))
				.andExpectAll(status().is3xxRedirection(), handler().methodName("saveCheck"), model().hasNoErrors(),
						redirectedUrl("/"))
				.andReturn();

		assertEquals(principal, result.getRequest().getUserPrincipal());

	}

	private ValidationDto createValidationDto(ClaimDto claim, ManagerDto manager, double sum) {
		return ValidationDto.builder().id(1L).claim(claim).manager(manager)
				.createdTime(LocalDateTime.of(2000, 1, 1, 12, 0))
				.compensation(monetaryService.getValue(BigDecimal.valueOf(sum))).build();
	}

	private ManagerDto createManagerDto(String managerLogin) {
		ManagerDto manager = ManagerDto.builder().id(1L)
				.credentials(new CredentialsDto(managerLogin, "pass".toCharArray())).firstName("Rebecca")
				.lastName("Johnson").birthDate(LocalDate.of(1980, 12, 1)).sex("FEMALE")
				.contacts(listOf("rebecca_johnson@gmail.com", "+1(555)555-35-55")).build();
		return manager;
	}

	private ClaimDto createClaimDto(Long claimId) {
		return ClaimDto.builder().id(claimId).bookReturn(createReturnDto()).book(createBookDto())
				.librarian(createLibrarianDto()).createdTime(LocalDateTime.of(1990, 1, 1, 12, 0))
				.damageDescription("something bad happened").build();
	}

	private BookDto createBookDto() {
		return BookDto.builder().id(1L).author("Jack London").title("Sea wolf").isbn("01234567")
				.publishDate(LocalDate.of(1980, 1, 1)).genre("FICTIONAL").country(new CountryDto(1L, "US"))
				.language(new LanguageDto(1L, "English")).pageCount(300).size("OCTAVO").coverType("HARD")
				.coverSurface("GLOSS").build();
	}

	private ReturnDto createReturnDto() {
		var returnDocument = ReturnDto.builder().id(1L).customer(createCustomerDto()).librarian(createLibrarianDto())
				.createdTime(LocalDateTime.of(2000, 1, 1, 1, 1)).build();
		returnDocument.books().addAll(sortedSetOf(createBookDtos()));
		return returnDocument;
	}

	private LibrarianDto createLibrarianDto() {
		var librarian = LibrarianDto.builder().id(1L)
				.credentials(new CredentialsDto("vera_cruise", "pass".toCharArray())).firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex("FEMALE")
				.contacts(listOf("vera_cruise@gmail.com", "+1(555)555-05-55")).build();
		return librarian;
	}

	private CustomerDto createCustomerDto() {
		var customer = CustomerDto.builder().id(1L).credentials(new CredentialsDto("john_smith", "pass".toCharArray()))
				.firstName("John").lastName("Smith").birthDate(LocalDate.of(1990, 1, 1)).sex("MALE")
				.contacts(listOf("john_smith@gmail.com", "+1(555)555-55-55")).build();
		return customer;
	}

	private BookDto[] createBookDtos() {
		CountryDto england = new CountryDto(1L, "England");
		CountryDto us = new CountryDto(2L, "US");

		LanguageDto english = new LanguageDto(1L, "English");
		var book1 = BookDto.builder().id(1L).author("Samuel Johnson").title("Those days").isbn("012345678")
				.publishDate(LocalDate.of(2015, 1, 1)).genre("FICTIONAL").country(england).language(english)
				.pageCount(100).size("QUARTO").coverType("HARD").coverSurface("UNCOATED").build();
		var book2 = BookDto.builder().id(2L).author("Kate Yank").title("Chippendale fantasies").isbn("876543210")
				.publishDate(LocalDate.of(2014, 2, 2)).genre("PHILOSOPHICAL").country(us).language(english)
				.pageCount(200).size("FOLIO").coverType("HARD").coverSurface("GLOSS").build();
		var book3 = BookDto.builder().id(3L).author("Troy Richter").title("Night and dawn").isbn("123456780")
				.publishDate(LocalDate.of(2013, 3, 3)).genre("HISTORICAL").country(us).language(english).pageCount(300)
				.size("DUODECIMO").coverType("SOFT").coverSurface("SILK").build();

		return new BookDto[] { book1, book2, book3 };
	}

}
