package com.streamlined.library.controller;

import static com.streamlined.library.Utilities.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.service.LibrarianService;

import jakarta.servlet.ServletException;

class LibrarianControllerTest {

	private MockMvc mvc;
	private AutoCloseable resource;

	private LibrarianController librarianController;

	@Mock
	private LibrarianService librarianService;

	@Mock
	private Principal principal;

	@BeforeEach
	void setUp() throws Exception {
		resource = MockitoAnnotations.openMocks(this);
		librarianController = new LibrarianController(librarianService);
		mvc = MockMvcBuilders.standaloneSetup(librarianController).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		resource.close();
	}

	@Test
	@DisplayName("should display form to edit personal data of librarian")
	void shouldDisplayLibrarianPersobalDataForm() throws Exception {

		final var librianId = 1L;
		final var librarianLogin = "librarian";
		final var librarianDto = createLibrarianDto(librianId, librarianLogin);

		when(principal.getName()).thenReturn(librarianLogin);
		when(librarianService.getLibrarianByLogin(anyString())).thenReturn(Optional.of(librarianDto));

		var result = mvc.perform(get("/librarian/edit-data").principal(principal))
				.andExpectAll(status().isOk(), view().name("edit-librarian"), model().attributeExists("sexList"),
						handler().methodName("editPersonalData"), model().hasNoErrors(),
						model().attribute("user", librarianDto))
				.andReturn();

		assertEquals(principal, result.getRequest().getUserPrincipal());

	}

	@Test
	@DisplayName("should throw exception when librarian not found by login")
	void shouldThrowExceptionWhenLibrarianNotFoundByLogin() throws Exception {

		final var librarianLogin = "librarian";

		when(principal.getName()).thenReturn(librarianLogin);
		when(librarianService.getLibrarianByLogin(anyString())).thenReturn(Optional.empty());

		try {
			mvc.perform(get("/librarian/edit-data").principal(principal));

			fail("should throw exception when librarian not found by login");
		} catch (ServletException e) {
			assertTrue(NoEntityFoundException.class.isInstance(e.getCause()),
					() -> "should be caused by NoEntityFoundException");
		}

	}

	@Test
	@DisplayName("should add new librarian entity for given DTO and empty id")
	void shouldSaveLibrarianForIdAndDTO() throws Exception {

		final var librarianLogin = "librarian";
		final var librarianId = 1L;
		final var librarianDto = createLibrarianDto(null, librarianLogin);
		final Librarian librarian = new Librarian();

		when(principal.getName()).thenReturn(librarianLogin);
		doAnswer(invocation -> {
			librarian.setId(librarianId);
			librarian.setLogin(librarianLogin);
			return null;
		}).when(librarianService).save(eq(null), any());

		var result = mvc
				.perform(post("/librarian/edit-data/").param("librarianDto", String.valueOf(librarianDto))
						.principal(principal))
				.andExpectAll(status().is3xxRedirection(), handler().methodName("savePersonalData"),
						model().hasNoErrors(), redirectedUrl("/"))
				.andReturn();

		assertNotNull(librarian);
		assertNotNull(librarian.getId());
		assertNotNull(librarian.getLogin());
		assertEquals(librarianId, librarian.getId());
		assertEquals(librarianLogin, librarian.getLogin());
		assertEquals(principal, result.getRequest().getUserPrincipal());

	}

	@Test
	@DisplayName("should update existing librarian entity for given DTO and non-empty id")
	void shouldUpdateLibrarianForIdAndDTO() throws Exception {

		final var librarianLogin = "librarian";
		final var librarianId = 1L;
		final var librarianDto = createLibrarianDto(librarianId, librarianLogin);
		final Librarian librarian = new Librarian();
		librarian.setId(librarianId);

		when(principal.getName()).thenReturn(librarianLogin);
		doAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			librarian.setId(id);
			librarian.setLogin(librarianLogin);
			return null;
		}).when(librarianService).save(anyLong(), any());

		var result = mvc
				.perform(post("/librarian/edit-data/").param("id", String.valueOf(librarianId))
						.param("librarianDto", String.valueOf(librarianDto)).principal(principal))
				.andExpectAll(status().is3xxRedirection(), handler().methodName("savePersonalData"),
						model().hasNoErrors(), redirectedUrl("/"))
				.andReturn();

		assertNotNull(librarian);
		assertNotNull(librarian.getId());
		assertNotNull(librarian.getLogin());
		assertEquals(librarianId, librarian.getId());
		assertEquals(librarianLogin, librarian.getLogin());
		assertEquals(principal, result.getRequest().getUserPrincipal());

	}

	@Test
	@DisplayName("should display new librarian registration form")
	void shouldDisplayNewLibrarianRegistrationForm() throws Exception {

		final var librarianLogin = "librarian";
		final var librarianDto = new LibrarianDto();

		when(principal.getName()).thenReturn(librarianLogin);
		when(librarianService.createNewLibrarian()).thenReturn(librarianDto);

		mvc.perform(get("/librarian/register-new").principal(principal)).andExpectAll(status().isOk(),
				view().name("register-new-librarian"), model().attribute("user", librarianDto),
				handler().methodName("registerNewLibrarian"), model().hasNoErrors());

	}

	private LibrarianDto createLibrarianDto(Long id, String librarianLogin) {
		CredentialsDto credentials = new CredentialsDto(librarianLogin);
		credentials.setPassword("pass".toCharArray());
		LibrarianDto librarian = LibrarianDto.builder().id(id).credentials(credentials).firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex("FEMALE")
				.contacts(listOf("vera_cruise@gmail.com", "+1(555)555-05-55")).build();
		return librarian;
	}

}
