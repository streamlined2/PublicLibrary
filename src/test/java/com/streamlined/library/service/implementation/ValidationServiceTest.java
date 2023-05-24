package com.streamlined.library.service.implementation;

import static com.streamlined.library.Utilities.listOf;
import static com.streamlined.library.Utilities.sortedSetOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ClaimRepository;
import com.streamlined.library.dao.ManagerRepository;
import com.streamlined.library.dao.ValidationRepository;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Claim;
import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Country;
import com.streamlined.library.model.Cover;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Language;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Manager;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.Return;
import com.streamlined.library.model.Validation;
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
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.ClaimMapper;
import com.streamlined.library.model.mapper.CountryMapper;
import com.streamlined.library.model.mapper.CustomerMapper;
import com.streamlined.library.model.mapper.LanguageMapper;
import com.streamlined.library.model.mapper.LibrarianMapper;
import com.streamlined.library.model.mapper.ManagerMapper;
import com.streamlined.library.model.mapper.ReturnMapper;
import com.streamlined.library.model.mapper.ValidationMapper;
import com.streamlined.library.service.MonetaryService;
import com.streamlined.library.service.ValidationService;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
class ValidationServiceTest {

	private static final String CURRENCY_CODE = "USD";

	@Mock
	private ManagerRepository managerRepository;
	@Mock
	private ClaimRepository claimRepository;
	@Mock
	private ValidationRepository validationRepository;

	private ValidationMapper validationMapper;

	private MonetaryService monetaryService;

	private ValidationService validationService;

	@Captor
	private ArgumentCaptor<Long> validationClaimCaptor;
	@Captor
	private ArgumentCaptor<Long> claimIdCaptor;
	@Captor
	private ArgumentCaptor<String> managerLoginCaptor;
	@Captor
	private ArgumentCaptor<Validation> validationCaptor;

	@BeforeEach
	void setUp() throws Exception {
		PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

		CountryMapper countryMapper = new CountryMapper();
		LanguageMapper languageMapper = new LanguageMapper();
		BookMapper bookMapper = new BookMapper(countryMapper, languageMapper);

		CustomerMapper customerMapper = new CustomerMapper(passwordEncoder);
		LibrarianMapper librarianMapper = new LibrarianMapper(passwordEncoder);
		ManagerMapper managerMapper = new ManagerMapper(passwordEncoder);

		ReturnMapper returnMapper = new ReturnMapper(customerMapper, librarianMapper, bookMapper);
		ClaimMapper claimMapper = new ClaimMapper(returnMapper, bookMapper, librarianMapper);

		validationMapper = new ValidationMapper(claimMapper, managerMapper);

		monetaryService = new DefaultMonetaryService(CURRENCY_CODE);

		validationService = new DefaultValidationService(managerRepository, claimRepository, validationRepository,
				validationMapper, monetaryService);
	}

	@Test
	@DisplayName("should throw exception if validation by claim and manager by login not found")
	void givenClaimIdManagerLogin_whenGetValidationByClaimAndValidationByClaimNotFoundAndManagerByLoginNotFound_thenThrowException() {
		
		when(validationRepository.findByClaim(anyLong())).thenReturn(Optional.empty());
		when(managerRepository.findByLogin(anyString())).thenReturn(Optional.empty());
		
		assertThrows(NoEntityFoundException.class, () -> validationService.getValidationByClaim(1L,"manager"));
		
		verify(validationRepository).findByClaim(anyLong());
		verify(managerRepository).findByLogin(anyString());
		verify(claimRepository, never()).findById(anyLong());
		verifyNoMoreInteractions(validationRepository, managerRepository, claimRepository);
		
	}

	@Test
	@DisplayName("should throw exception if validation by claim and claim not found")
	void givenClaimIdManagerLogin_whenGetValidationByClaimAndValidationByClaimNotFoundAndClaimNotFound_thenThrowException() {

		final String managerLogin = "rebecca_johnson";
		var manager = createManager(managerLogin);

		when(validationRepository.findByClaim(anyLong())).thenReturn(Optional.empty());
		when(managerRepository.findByLogin(anyString())).thenReturn(Optional.of(manager));
		when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoEntityFoundException.class, () -> validationService.getValidationByClaim(1L, "manager"));

		verify(validationRepository).findByClaim(anyLong());
		verify(managerRepository).findByLogin(anyString());
		verify(claimRepository).findById(anyLong());
		verifyNoMoreInteractions(validationRepository, managerRepository, claimRepository);

	}

	@Test
	@DisplayName("should return blank validation if validation by claim not found")
	void givenClaimIdManagerLogin_whenGetValidationByClaimAndValidationByClaimNotFoundAndManagerFoundAndClaimFound_thenReturnBlankValidation() {

		final Long claimId = 1L;
		final String managerLogin = "rebecca_johnson";

		var suppliedClaim = createClaim(claimId);
		var suppliedManager = createManager(managerLogin);

		var expectedClaimDto = createClaimDto(claimId);
		var expectedManagerDto = createManagerDto(managerLogin);

		when(validationRepository.findByClaim(anyLong())).thenReturn(Optional.empty());
		when(managerRepository.findByLogin(anyString())).thenReturn(Optional.of(suppliedManager));
		when(claimRepository.findById(anyLong())).thenReturn(Optional.of(suppliedClaim));

		var actualValidationDto = validationService.getValidationByClaim(claimId, managerLogin);

		verify(validationRepository).findByClaim(anyLong());
		verify(managerRepository).findByLogin(anyString());
		verify(claimRepository).findById(anyLong());
		verifyNoMoreInteractions(validationRepository, managerRepository, claimRepository);

		assertNull(actualValidationDto.id());
		assertEquals(expectedClaimDto, actualValidationDto.claim());
		assertEquals(expectedManagerDto, actualValidationDto.manager());
		assertNull(actualValidationDto.createdTime());
		assertEquals(0L, actualValidationDto.compensation().getNumber().longValue());

	}

	@Test
	@DisplayName("should return validation found by claim")
	void givenClaimId_whenGetValidationByClaimAndValidationByClaimFound_thenReturnValidation() {

		final Long claimId = 1L;
		final String managerLogin = "rebecca_johnson";
		final double sum = 100.00;

		var suppliedClaim = createClaim(claimId);
		var suppliedManager = createManager(managerLogin);
		var suppliedValidation = createValidation(suppliedClaim, suppliedManager, sum);
		final var createdTime = suppliedValidation.getCreatedTime();
		final var validationId = suppliedValidation.getId();

		var expectedClaimDto = createClaimDto(claimId);
		var expectedManagerDto = createManagerDto(managerLogin);

		when(validationRepository.findByClaim(anyLong())).thenReturn(Optional.of(suppliedValidation));
		when(managerRepository.findByLogin(anyString())).thenReturn(Optional.of(suppliedManager));
		when(claimRepository.findById(anyLong())).thenReturn(Optional.of(suppliedClaim));

		var actualValidationDto = validationService.getValidationByClaim(claimId, managerLogin);

		verify(validationRepository).findByClaim(validationClaimCaptor.capture());
		verify(managerRepository).findByLogin(managerLoginCaptor.capture());
		verify(claimRepository).findById(claimIdCaptor.capture());
		verifyNoMoreInteractions(validationRepository, managerRepository, claimRepository);

		assertEquals(claimId, claimIdCaptor.getValue());
		assertEquals(claimId, actualValidationDto.id());

		assertEquals(managerLogin, managerLoginCaptor.getValue());
		assertEquals(managerLogin, actualValidationDto.manager().getCredentials().getLogin());

		assertNotNull(actualValidationDto.id());
		assertEquals(validationId, actualValidationDto.id());
		assertEquals(expectedClaimDto, actualValidationDto.claim());
		assertEquals(expectedManagerDto, actualValidationDto.manager());
		assertEquals(createdTime, actualValidationDto.createdTime());
		assertEquals(monetaryService.getValue(BigDecimal.valueOf(sum)), actualValidationDto.compensation());

	}

	@Test
	@DisplayName("should throw an exception on attempt to save validation when claim not found")
	void givenClaimIdAndValidation_whenSaveValidationAndClaimNotFound_thenThrowException() {

		final Long claimId = 1L;
		final String managerLogin = "rebecca_johnson";
		final var claimDto = createClaimDto(claimId);
		final var managerDto = createManagerDto(managerLogin);
		final double sum = 100.00;
		final var suppliedValidationDto = createValidationDto(claimDto, managerDto, sum);

		when(claimRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoEntityFoundException.class,
				() -> validationService.saveValidation(claimId, suppliedValidationDto));

		verify(claimRepository).findById(anyLong());
		verify(validationRepository, never()).save(any());
		verifyNoMoreInteractions(claimRepository, validationRepository);

	}

	@Test
	@DisplayName("should save validation when claim found")
	void givenClaimIdAndValidation_whenSaveValidationAndClaimFound_thenSaveValidation() {

		final var claimId = 1L;
		final var managerLogin = "rebecca_johnson";
		final var claim = createClaim(claimId);
		final var claimDto = createClaimDto(claimId);
		final var managerDto = createManagerDto(managerLogin);
		final double sum = 100.00;
		final var suppliedValidationDto = createValidationDto(claimDto, managerDto, sum);
		final var validationId = 1L;

		when(claimRepository.findById(anyLong())).thenReturn(Optional.of(claim));

		doAnswer(invocation -> {
			Validation entity = invocation.getArgument(0);
			entity.setId(validationId);
			return null;
		}).when(validationRepository).save(any());

		validationService.saveValidation(claimId, suppliedValidationDto);

		verify(claimRepository).findById(claimIdCaptor.capture());
		verify(validationRepository).save(validationCaptor.capture());
		verifyNoMoreInteractions(claimRepository, validationRepository);

		assertEquals(claimId, claimIdCaptor.getValue());
		assertEquals(validationId, validationCaptor.getValue().getId());
		assertEquals(claim, validationCaptor.getValue().getClaim());
		assertNull(validationCaptor.getValue().getManager());
		assertNull(validationCaptor.getValue().getCreatedTime());
		assertEquals(monetaryService.getValue(BigDecimal.valueOf(sum)), validationCaptor.getValue().getCompensation());

	}

	private ValidationDto createValidationDto(ClaimDto claim, ManagerDto manager, double sum) {
		return ValidationDto.builder().id(1L).claim(claim).manager(manager)
				.createdTime(LocalDateTime.of(2000, 1, 1, 12, 0))
				.compensation(monetaryService.getValue(BigDecimal.valueOf(sum))).build();
	}

	private Validation createValidation(Claim claim, Manager manager, double sum) {
		return Validation.builder().id(1L).claim(claim).manager(manager)
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

	private ReturnDto createReturnDto() {
		var returnDocument = ReturnDto.builder().id(1L).customer(createCustomerDto()).librarian(createLibrarianDto())
				.createdTime(LocalDateTime.of(2000, 1, 1, 1, 1)).build();
		returnDocument.books().addAll(sortedSetOf(createBookDtos()));
		return returnDocument;
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

	private BookDto createBookDto() {
		return BookDto.builder().id(1L).author("Jack London").title("Sea wolf").isbn("01234567")
				.publishDate(LocalDate.of(1980, 1, 1)).genre("FICTIONAL").country(new CountryDto(1L, "US"))
				.language(new LanguageDto(1L, "English")).pageCount(300).size("OCTAVO").coverType("HARD")
				.coverSurface("GLOSS").build();
	}

	private Claim createClaim(Long claimId) {
		return Claim.builder().id(claimId).bookReturn(createBookReturn()).book(createBook())
				.librarian(createLibrarian()).createdTime(LocalDateTime.of(1990, 1, 1, 12, 0))
				.damageDescription("something bad happened").build();
	}

	private Book createBook() {
		return Book.builder().id(1L).author("Jack London").title("Sea wolf").isbn("01234567")
				.publishDate(LocalDate.of(1980, 1, 1)).genre(Genre.FICTIONAL).country(new Country(1L, "US"))
				.language(createLanguage()).pageCount(300).size(Size.OCTAVO)
				.cover(new Cover(Cover.Type.HARD, Cover.Surface.GLOSS)).build();
	}

	private Language createLanguage() {
		return new Language(1L, "English");
	}

	private Return createBookReturn() {
		var returnDocument = Return.builder().id(1L).customer(createCustomer()).librarian(createLibrarian())
				.createdTime(LocalDateTime.of(2000, 1, 1, 1, 1)).build();
		returnDocument.getBooks().addAll(listOf(createBooks()));
		return returnDocument;
	}

	private Book[] createBooks() {
		Country england = new Country(1L, "England");
		Country us = new Country(2L, "US");

		Language english = createLanguage();
		Book book1 = Book.builder().id(1L).author("Samuel Johnson").title("Those days").isbn("012345678")
				.publishDate(LocalDate.of(2015, 1, 1)).genre(Genre.FICTIONAL).country(england).language(english)
				.pageCount(100).size(Size.QUARTO).cover(new Cover(Cover.Type.HARD, Cover.Surface.UNCOATED)).build();
		Book book2 = Book.builder().id(2L).author("Kate Yank").title("Chippendale fantasies").isbn("876543210")
				.publishDate(LocalDate.of(2014, 2, 2)).genre(Genre.PHILOSOPHICAL).country(us).language(english)
				.pageCount(200).size(Size.FOLIO).cover(new Cover(Cover.Type.HARD, Cover.Surface.GLOSS)).build();
		Book book3 = Book.builder().id(3L).author("Troy Richter").title("Night and dawn").isbn("123456780")
				.publishDate(LocalDate.of(2013, 3, 3)).genre(Genre.HISTORICAL).country(us).language(english)
				.pageCount(300).size(Size.DUODECIMO).cover(new Cover(Cover.Type.SOFT, Cover.Surface.SILK)).build();

		return new Book[] { book1, book2, book3 };
	}

	private Customer createCustomer() {
		Customer customer = Customer.builder().id(1L).login("john_smith").passwordHash("pass").firstName("John")
				.lastName("Smith").birthDate(LocalDate.of(1990, 1, 1)).sex(Sex.MALE).build();
		customer.getContacts()
				.addAll(sortedSetOf(Contact.create("john_smith@gmail.com"), Contact.create("+1(555)555-55-55")));
		return customer;
	}

	private Librarian createLibrarian() {
		Librarian librarian = Librarian.builder().id(1L).login("vera_cruise").passwordHash("pass").firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex(Sex.FEMALE).build();
		librarian.getContacts()
				.addAll(sortedSetOf(Contact.create("vera_cruise@gmail.com"), Contact.create("+1(555)555-05-55")));
		return librarian;
	}

	private Manager createManager(String managerLogin) {
		Manager manager = Manager.builder().id(1L).login(managerLogin).passwordHash("pass").firstName("Rebecca")
				.lastName("Johnson").birthDate(LocalDate.of(1980, 12, 1)).sex(Sex.FEMALE).build();
		manager.getContacts()
				.addAll(sortedSetOf(Contact.create("rebecca_johnson@gmail.com"), Contact.create("+1(555)555-35-55")));
		return manager;
	}

}
