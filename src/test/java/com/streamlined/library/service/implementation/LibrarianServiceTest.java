package com.streamlined.library.service.implementation;

import static com.streamlined.library.Utilities.sortedSetOf;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.model.mapper.LibrarianMapper;
import static com.streamlined.library.Utilities.listOf;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
class LibrarianServiceTest {

	@Mock
	private LibrarianRepository librarianRepository;

	private DefaultLibrarianService librarianService;

	private PasswordEncoder passwordEncoder;

	private LibrarianMapper librarianMapper;

	@Captor
	private ArgumentCaptor<Librarian> entityCaptor;

	@BeforeEach
	void setUp() throws Exception {
		passwordEncoder = NoOpPasswordEncoder.getInstance();
		librarianMapper = new LibrarianMapper(passwordEncoder);
		librarianService = new DefaultLibrarianService(librarianRepository, librarianMapper);
	}

	@Test
	@DisplayName("should return nothing if librarian not found by login")
	void givenUserLogin_whenGetLibrarianByLoginAndNotFound_thenReturnNothing() {

		when(librarianRepository.findByLogin(anyString())).thenReturn(Optional.empty());
		
		Optional<LibrarianDto> librarian = librarianService.getLibrarianByLogin("");
		verifyNoMoreInteractions(librarianRepository);
		
		assertTrue(librarian.isEmpty());		
	}

	@Test
	@DisplayName("should find librarian by login")
	void givenUserLogin_whenGetLibrarianByLoginAndFound_thenReturnLibrarian() {
		final Long librarianId = 1L;
		final String librarianLogin = "vera_cruise";
		
		Librarian suppliedLibrarian = constructLibrarian(librarianId, librarianLogin);
		LibrarianDto expectedLibrarianDto = constructLibrarianDto(librarianId, librarianLogin);

		when(librarianRepository.findByLogin(anyString())).thenReturn(Optional.of(suppliedLibrarian));

		var actualLibrarianDto = librarianService.getLibrarianByLogin(librarianLogin);

		verify(librarianRepository).findByLogin(anyString());
		verifyNoMoreInteractions(librarianRepository);

		assertTrue(actualLibrarianDto.isPresent());
		assertEquals(librarianLogin, actualLibrarianDto.get().getCredentials().getLogin());
		assertEquals(expectedLibrarianDto, actualLibrarianDto.get());

	}

	@Test
	@DisplayName("should return list of all librarians")
	void givenLibrarianList_whenGetAllLibrarians_thenReturnLibrarianList() {
		var suppliedLibrarianEntitiesList = prepareListOfLibrarianEntities();
		var expectedLibrarianDtoList = prepareListOfLibrarianDtos();

		when(librarianRepository.findAll()).thenReturn(suppliedLibrarianEntitiesList);

		Stream<LibrarianDto> actualLibrarianDtoList = librarianService.getAllLibrarians();

		verify(librarianRepository).findAll();
		verifyNoMoreInteractions(librarianRepository);

		assertEquals(expectedLibrarianDtoList.collect(Collectors.toList()),
				actualLibrarianDtoList.collect(Collectors.toList()));

	}

	@Test
	@DisplayName("should return nothing if librarian not found by id")
	void givenUserId_whenGetLibrarianByIdAndNotFound_thenReturnNothing() {

		when(librarianRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		Optional<LibrarianDto> librarian = librarianService.getLibrarianById(1L);
		verifyNoMoreInteractions(librarianRepository);
		
		assertTrue(librarian.isEmpty());		
	}

	@Test
	@DisplayName("should find librarian by id")
	void givenUserId_whenGetLibrarianByIdAndFound_thenReturnLibrarian() {
		final Long librarianId = 1L;
		final String librarianLogin = "user";
		Librarian suppliedLibrarian = constructLibrarian(librarianId, librarianLogin);
		LibrarianDto expectedLibrarianDto = constructLibrarianDto(librarianId, librarianLogin);

		when(librarianRepository.findById(anyLong())).thenReturn(Optional.of(suppliedLibrarian));

		var actualLibrarianDto = librarianService.getLibrarianById(librarianId);

		verify(librarianRepository).findById(anyLong());
		verifyNoMoreInteractions(librarianRepository);

		assertTrue(actualLibrarianDto.isPresent());
		assertEquals(librarianId, actualLibrarianDto.get().getId());
		assertEquals(expectedLibrarianDto, actualLibrarianDto.get());

	}

	@Test
	@DisplayName("should return new empty librarian instance")
	void given_whenCreateNewLibrarian_thenReturnNewLibrarianInstance() {

		var actualLibrarianDto = librarianService.createNewLibrarian();

		assertNotNull(actualLibrarianDto);
		assertNull(actualLibrarianDto.getId());
		assertNull(actualLibrarianDto.getCredentials());
		assertNull(actualLibrarianDto.getFirstName());
		assertNull(actualLibrarianDto.getLastName());
		assertNull(actualLibrarianDto.getBirthDate());
		assertNull(actualLibrarianDto.getSex());
		assertTrue(actualLibrarianDto.getContacts().isEmpty());

	}

	@Test
	@DisplayName("should throw exception if null DTO argument passed")
	void givenNull_whenSave_thenThrowException() {

		assertThrows(NullPointerException.class, () -> librarianService.save(1L, null));

	}

	@Test
	@DisplayName("should update existing librarian entity")
	void givenLibrarian_whenSaveAndEntityExists_thenUpdateExistingLibrarianEntity() {

		final Long expectedLibrarianId = 1L;
		final String expectedLibrarianLogin = "login";
		final LibrarianDto expectedLibrarianDto = constructLibrarianDto(null, expectedLibrarianLogin);

		librarianService.save(expectedLibrarianId, expectedLibrarianDto);

		verify(librarianRepository).save(entityCaptor.capture());
		verifyNoMoreInteractions(librarianRepository);

		assertEquals(expectedLibrarianId, entityCaptor.getValue().getId());
		assertEquals(expectedLibrarianDto.getCredentials().getLogin(), entityCaptor.getValue().getLogin());
		assertArrayEquals(expectedLibrarianDto.getCredentials().getPassword(),
				entityCaptor.getValue().getPasswordHash().toCharArray());
		assertEquals(expectedLibrarianDto.getFirstName(), entityCaptor.getValue().getFirstName());
		assertEquals(expectedLibrarianDto.getLastName(), entityCaptor.getValue().getLastName());
		assertEquals(expectedLibrarianDto.getBirthDate(), entityCaptor.getValue().getBirthDate());
		assertEquals(expectedLibrarianDto.getSex(), entityCaptor.getValue().getSex().name());
		assertEquals(expectedLibrarianDto.getContacts(),
				entityCaptor.getValue().getContacts().stream().map(Contact::getContactInfo).toList());

	}

	@Test
	@DisplayName("should save new librarian entity")
	void givenLibrarian_whenSaveAndEntityDontExist_thenSaveNewLibrarianEntity() {

		final Long expectedLibrarianId = 1L;
		final String expectedLibrarianLogin = "login";
		final LibrarianDto expectedLibrarianDto = constructLibrarianDto(null, expectedLibrarianLogin);

		doAnswer(invocation -> {
			Librarian librarianEntity = invocation.getArgument(0);
			librarianEntity.setId(expectedLibrarianId);
			return null;
		}).when(librarianRepository).save(any());

		librarianService.save(null, expectedLibrarianDto);

		verify(librarianRepository).save(entityCaptor.capture());
		verifyNoMoreInteractions(librarianRepository);

		assertEquals(expectedLibrarianId, entityCaptor.getValue().getId());
		assertEquals(expectedLibrarianDto.getCredentials().getLogin(), entityCaptor.getValue().getLogin());
		assertArrayEquals(expectedLibrarianDto.getCredentials().getPassword(),
				entityCaptor.getValue().getPasswordHash().toCharArray());
		assertEquals(expectedLibrarianDto.getFirstName(), entityCaptor.getValue().getFirstName());
		assertEquals(expectedLibrarianDto.getLastName(), entityCaptor.getValue().getLastName());
		assertEquals(expectedLibrarianDto.getBirthDate(), entityCaptor.getValue().getBirthDate());
		assertEquals(expectedLibrarianDto.getSex(), entityCaptor.getValue().getSex().name());
		assertEquals(expectedLibrarianDto.getContacts(),
				entityCaptor.getValue().getContacts().stream().map(Contact::getContactInfo).toList());

	}

	private Stream<LibrarianDto> prepareListOfLibrarianDtos() {
		LibrarianDto librarian1 = LibrarianDto.builder().id(1L)
				.credentials(CredentialsDto.builder().login("vera_cruise").password("pass".toCharArray()).build())
				.firstName("Vera").lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex("FEMALE")
				.contacts(listOf("vera_cruise@gmail.com", "+1(555)555-05-55")).build();

		LibrarianDto librarian2 = LibrarianDto.builder().id(2L)
				.credentials(CredentialsDto.builder().login("richard_foe").password("pass".toCharArray()).build())
				.firstName("Richard").lastName("Foe").birthDate(LocalDate.of(1990, 6, 4)).sex("MALE")
				.contacts(listOf("richard_foe@gmail.com", "+1(555)545-05-55")).build();

		LibrarianDto librarian3 = LibrarianDto.builder().id(3L)
				.credentials(CredentialsDto.builder().login("jane_truth").password("pass".toCharArray()).build())
				.firstName("Jane").lastName("Truth").birthDate(LocalDate.of(1980, 7, 3)).sex("FEMALE")
				.contacts(listOf("jane_truth@gmail.com", "+1(555)355-05-55")).build();

		return Stream.of(librarian1, librarian2, librarian3);
	}

	private Iterable<Librarian> prepareListOfLibrarianEntities() {
		Librarian librarian1 = Librarian.builder().id(1L).login("vera_cruise").passwordHash("pass").firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex(Sex.FEMALE).build();
		librarian1.getContacts()
				.addAll(sortedSetOf(Contact.create("vera_cruise@gmail.com"), Contact.create("+1(555)555-05-55")));

		Librarian librarian2 = Librarian.builder().id(2L).login("richard_foe").passwordHash("pass").firstName("Richard")
				.lastName("Foe").birthDate(LocalDate.of(1990, 6, 4)).sex(Sex.MALE).build();
		librarian2.getContacts()
				.addAll(sortedSetOf(Contact.create("richard_foe@gmail.com"), Contact.create("+1(555)545-05-55")));

		Librarian librarian3 = Librarian.builder().id(3L).login("jane_truth").passwordHash("pass").firstName("Jane")
				.lastName("Truth").birthDate(LocalDate.of(1980, 7, 3)).sex(Sex.FEMALE).build();
		librarian3.getContacts()
				.addAll(sortedSetOf(Contact.create("jane_truth@gmail.com"), Contact.create("+1(555)355-05-55")));

		return List.of(librarian1, librarian2, librarian3);
	}

	private Librarian constructLibrarian(Long id, String login) {
		Librarian librarian = Librarian.builder().id(id).login(login).passwordHash("pass").firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex(Sex.FEMALE).build();
		librarian.getContacts()
				.addAll(sortedSetOf(Contact.create("vera_cruise@gmail.com"), Contact.create("+1(555)555-05-55")));
		return librarian;
	}

	private LibrarianDto constructLibrarianDto(Long id, String librarianLogin) {
		CredentialsDto credentials = new CredentialsDto(librarianLogin);
		credentials.setPassword("pass".toCharArray());
		LibrarianDto librarian = LibrarianDto.builder().id(id).credentials(credentials).firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex("FEMALE")
				.contacts(listOf("vera_cruise@gmail.com", "+1(555)555-05-55")).build();
		return librarian;
	}

}
