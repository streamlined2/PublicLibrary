package com.streamlined.library.service.implementation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Streamable;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.ApprovalRepository;
import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.RequestRepository;
import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Book.Genre;
import com.streamlined.library.model.Book.Size;
import com.streamlined.library.model.Contact;
import com.streamlined.library.model.Country;
import com.streamlined.library.model.Cover;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Language;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Person.Sex;
import com.streamlined.library.model.Request;
import com.streamlined.library.model.dto.ApprovalDto;
import com.streamlined.library.model.dto.BookDto;
import com.streamlined.library.model.dto.CountryDto;
import com.streamlined.library.model.dto.CredentialsDto;
import com.streamlined.library.model.dto.CustomerDto;
import com.streamlined.library.model.dto.LanguageDto;
import com.streamlined.library.model.dto.LibrarianDto;
import com.streamlined.library.model.dto.RequestDto;
import com.streamlined.library.model.mapper.ApprovalMapper;
import com.streamlined.library.model.mapper.BookMapper;
import com.streamlined.library.model.mapper.CountryMapper;
import com.streamlined.library.model.mapper.CustomerMapper;
import com.streamlined.library.model.mapper.LanguageMapper;
import com.streamlined.library.model.mapper.LibrarianMapper;
import com.streamlined.library.model.mapper.RequestMapper;

import static com.streamlined.library.Utilities.listOf;
import static com.streamlined.library.Utilities.sortedSetOf;

@SuppressWarnings("deprecation")
@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {

	@Mock
	private ApprovalRepository approvalRepository;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private RequestRepository requestRepository;

	private DefaultApprovalService approvalService;

	@Captor
	private ArgumentCaptor<Long> idCaptor;
	@Captor
	private ArgumentCaptor<List<Long>> bookIdListCaptor;
	@Captor
	private ArgumentCaptor<Approval> approvalCaptor;

	private PasswordEncoder passwordEncoder;

	private ApprovalMapper approvalMapper;
	private RequestMapper requestMapper;
	private LibrarianMapper librarianMapper;
	private BookMapper bookMapper;
	private CountryMapper countryMapper;
	private LanguageMapper languageMapper;
	private CustomerMapper customerMapper;

	@BeforeEach
	void setUp() throws Exception {
		passwordEncoder = NoOpPasswordEncoder.getInstance();
		countryMapper = new CountryMapper();
		languageMapper = new LanguageMapper();
		librarianMapper = new LibrarianMapper(passwordEncoder);
		customerMapper = new CustomerMapper(passwordEncoder);
		bookMapper = new BookMapper(countryMapper, languageMapper);
		requestMapper = new RequestMapper(customerMapper, bookMapper);
		approvalMapper = new ApprovalMapper(bookMapper, requestMapper, librarianMapper);

		approvalService = new DefaultApprovalService(bookRepository, requestRepository, approvalRepository,
				approvalMapper);
	}

	@Test
	@DisplayName("should return list of approved requests")
	void givenApprovedRequestsList_whenGetApprovedRequests_thenReturnApprovedRequestsList() {
		var suppliedApprovalEntities = prepareListOfApprovalEntities();
		var expectedApprovalDTOs = prepareListOfApprovalDTOs();

		when(approvalRepository.getApprovedRequests()).thenReturn(suppliedApprovalEntities);

		var actualApprovalDTOs = approvalService.getApprovedRequests();

		verify(approvalRepository).getApprovedRequests();
		verifyNoMoreInteractions(bookRepository, requestRepository, approvalRepository);

		assertEquals(expectedApprovalDTOs.collect(Collectors.toList()),
				actualApprovalDTOs.collect(Collectors.toList()));

	}

	@Test
	@DisplayName("should find approval by passed id and return it")
	void givenApprovalId_whenGetApprovalById_thenReturnFoundApprovalEntity() {
		final Long approvalId = 1L;

		var suppliedApprovalEntity = prepareApprovalEntity(approvalId);
		var expectedApprovalDTO = prepareApprovalDTO(approvalId);

		when(approvalRepository.findById(anyLong())).thenReturn(suppliedApprovalEntity);

		var actualApprovalDTO = approvalService.getApprovalById(approvalId);

		verify(approvalRepository).findById(idCaptor.capture());
		verifyNoMoreInteractions(bookRepository, requestRepository, approvalRepository);

		assertEquals(expectedApprovalDTO, actualApprovalDTO);
		assertEquals(approvalId, idCaptor.getValue());

	}

	@Test
	@DisplayName("should throw exception on attempt to save approval when request not found")
	void givenRequestIdAndBookList_whenSaveApprovalAndRequestNotFound_thenThrowException() {
		
		when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		var bookList = List.<Long>of();
		assertThrows(NoEntityFoundException.class, () -> approvalService.saveApproval(1L, bookList));
		
		verify(requestRepository).findById(anyLong());
		
		verifyNoMoreInteractions(bookRepository, requestRepository, approvalRepository);
		
	}

	@Test
	@DisplayName("should throw exception if passed book list is invalid")
	void givenRequestIdAndBookList_whenSaveApprovalAndRequestFoundAndBookListInvalid_thenThrowException() {

		Request suppliedRequest = Request.builder().build();

		when(requestRepository.findById(anyLong())).thenReturn(Optional.of(suppliedRequest));
		when(bookRepository.findAllById(Mockito.<Long>anyIterable())).thenThrow(IllegalArgumentException.class);

		var bookList = List.<Long>of();
		assertThrows(IllegalArgumentException.class, () -> approvalService.saveApproval(1L, bookList));

		verify(requestRepository).findById(anyLong());
		verify(bookRepository).findAllById(Mockito.<Long>anyIterable());

		verifyNoMoreInteractions(bookRepository, requestRepository, approvalRepository);

	}

	@Test
	@DisplayName("should save approval with given request id and book list")
	void givenRequestIdAndBookList_whenSaveApprovalAndRequestFoundAndBookListValid_thenSaveApproval() {

		final Long expectedApprovalId = 1L;
		final Long requestId = 1L;
		final List<Long> bookIdList = List.of(1L, 2L, 3L);

		Request suppliedRequest = prepareRequestForSaveApproval(requestId);

		var bookList = prepareBookListForSaveApproval();

		Approval expectedApproval = prepareApprovalForSaveApproval(suppliedRequest, bookList);

		when(requestRepository.findById(anyLong())).thenReturn(Optional.of(suppliedRequest));
		when(bookRepository.findAllById(Mockito.<Long>anyIterable())).thenReturn(List.of(bookList));

		doAnswer(invocation -> {
			Approval entity = invocation.getArgument(0);
			entity.setId(expectedApprovalId);
			return null;
		}).when(approvalRepository).save(any());

		approvalService.saveApproval(suppliedRequest.getId(), bookIdList);

		verify(requestRepository).findById(idCaptor.capture());
		verify(bookRepository).findAllById(bookIdListCaptor.capture());
		verify(approvalRepository).save(approvalCaptor.capture());

		assertEquals(expectedApprovalId, approvalCaptor.getValue().getId());
		assertEquals(requestId, idCaptor.getValue());
		assertEquals(bookIdList, bookIdListCaptor.getValue());
		expectedApproval.setId(expectedApprovalId);
		assertEquals(expectedApproval, approvalCaptor.getValue());

		verifyNoMoreInteractions(bookRepository, requestRepository, approvalRepository);

	}

	private Approval prepareApprovalForSaveApproval(Request suppliedRequest, Book[] bookList) {
		Librarian librarian = Librarian.builder().id(1L).login("vera_cruise").passwordHash("pass").firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex(Sex.FEMALE).build();
		librarian.getContacts()
				.addAll(sortedSetOf(Contact.create("vera_cruise@gmail.com"), Contact.create("+1(555)555-05-55")));

		Approval expectedApproval = new Approval(null, suppliedRequest, librarian, LocalDateTime.of(2020, 4, 4, 6, 20));
		expectedApproval.getBooks().addAll(sortedSetOf(bookList));
		return expectedApproval;
	}

	private Book[] prepareBookListForSaveApproval() {
		Country england = new Country(1L, "England");
		Country us = new Country(2L, "US");

		Language english = new Language(1L, "English");
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

	private Request prepareRequestForSaveApproval(final Long requestId) {
		Customer customer = Customer.builder().id(1L).login("john_smith").passwordHash("pass").firstName("John")
				.lastName("Smith").birthDate(LocalDate.of(1990, 1, 1)).sex(Sex.MALE).build();
		customer.getContacts()
				.addAll(sortedSetOf(Contact.create("john_smith@gmail.com"), Contact.create("+1(555)555-55-55")));

		Request suppliedRequest = Request.builder().id(requestId).createdTime(LocalDateTime.of(2020, 1, 1, 12, 0))
				.customer(customer).build();
		return suppliedRequest;
	}

	private Optional<ApprovalDto> prepareApprovalDTO(Long id) {
		CustomerDto customer = CustomerDto.builder().id(1L)
				.credentials(new CredentialsDto("john_smith", "pass".toCharArray())).firstName("John").lastName("Smith")
				.birthDate(LocalDate.of(1990, 1, 1)).sex("MALE")
				.contacts(listOf("john_smith@gmail.com", "+1(555)555-55-55")).build();

		CountryDto england = new CountryDto(1L, "England");
		CountryDto us = new CountryDto(2L, "US");

		LanguageDto english = new LanguageDto(1L, "English");

		BookDto book1 = BookDto.builder().id(1L).author("Samuel Johnson").title("Those days").isbn("012345678")
				.publishDate(LocalDate.of(2015, 1, 1)).genre("FICTIONAL").country(england).language(english)
				.pageCount(100).size("QUARTO").coverType("HARD").coverSurface("UNCOATED").build();

		BookDto book2 = BookDto.builder().id(2L).author("Kate Yank").title("Chippendale fantasies").isbn("876543210")
				.publishDate(LocalDate.of(2014, 2, 2)).genre("PHILOSOPHICAL").country(us).language(english)
				.pageCount(200).size("FOLIO").coverType("HARD").coverSurface("GLOSS").build();

		BookDto book3 = BookDto.builder().id(3L).author("Troy Richter").title("Night and dawn").isbn("123456780")
				.publishDate(LocalDate.of(2013, 3, 3)).genre("HISTORICAL").country(us).language(english).pageCount(300)
				.size("DUODECIMO").coverType("SOFT").coverSurface("SILK").build();

		RequestDto request = RequestDto.builder().id(1L).createdTime(LocalDateTime.of(2020, 1, 1, 12, 0))
				.customer(customer).build();
		request.books().addAll(sortedSetOf(book1, book2, book3));

		LibrarianDto librarian = LibrarianDto.builder().id(1L)
				.credentials(new CredentialsDto("vera_cruise", "pass".toCharArray())).firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex("FEMALE")
				.contacts(listOf("vera_cruise@gmail.com", "+1(555)555-05-55")).build();

		ApprovalDto approval = ApprovalDto.builder().id(id).request(request).librarian(librarian)
				.createdTime(LocalDateTime.of(2020, 4, 4, 6, 20)).build();
		approval.books().addAll(sortedSetOf(book1, book2, book3));

		return Optional.of(approval);
	}

	private Optional<Approval> prepareApprovalEntity(Long id) {
		Customer customer = Customer.builder().id(1L).login("john_smith").passwordHash("pass").firstName("John")
				.lastName("Smith").birthDate(LocalDate.of(1990, 1, 1)).sex(Sex.MALE).build();
		customer.getContacts()
				.addAll(sortedSetOf(Contact.create("john_smith@gmail.com"), Contact.create("+1(555)555-55-55")));

		Country england = new Country(1L, "England");
		Country us = new Country(2L, "US");

		Language english = new Language(1L, "English");
		Book book1 = Book.builder().id(1L).author("Samuel Johnson").title("Those days").isbn("012345678")
				.publishDate(LocalDate.of(2015, 1, 1)).genre(Genre.FICTIONAL).country(england).language(english)
				.pageCount(100).size(Size.QUARTO).cover(new Cover(Cover.Type.HARD, Cover.Surface.UNCOATED)).build();
		Book book2 = Book.builder().id(2L).author("Kate Yank").title("Chippendale fantasies").isbn("876543210")
				.publishDate(LocalDate.of(2014, 2, 2)).genre(Genre.PHILOSOPHICAL).country(us).language(english)
				.pageCount(200).size(Size.FOLIO).cover(new Cover(Cover.Type.HARD, Cover.Surface.GLOSS)).build();
		Book book3 = Book.builder().id(3L).author("Troy Richter").title("Night and dawn").isbn("123456780")
				.publishDate(LocalDate.of(2013, 3, 3)).genre(Genre.HISTORICAL).country(us).language(english)
				.pageCount(300).size(Size.DUODECIMO).cover(new Cover(Cover.Type.SOFT, Cover.Surface.SILK)).build();

		Request request = Request.builder().id(1L).createdTime(LocalDateTime.of(2020, 1, 1, 12, 0)).customer(customer)
				.build();
		request.getBooks().addAll(sortedSetOf(book1, book2, book3));

		Librarian librarian = Librarian.builder().id(1L).login("vera_cruise").passwordHash("pass").firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex(Sex.FEMALE).build();
		librarian.getContacts()
				.addAll(sortedSetOf(Contact.create("vera_cruise@gmail.com"), Contact.create("+1(555)555-05-55")));

		Approval approval = new Approval(id, request, librarian, LocalDateTime.of(2020, 4, 4, 6, 20));
		approval.getBooks().addAll(sortedSetOf(book1, book2, book3));

		return Optional.of(approval);
	}

	private Stream<ApprovalDto> prepareListOfApprovalDTOs() {
		CustomerDto customer1 = CustomerDto.builder().id(1L)
				.credentials(new CredentialsDto("john_smith", "pass".toCharArray())).firstName("John").lastName("Smith")
				.birthDate(LocalDate.of(1990, 1, 1)).sex("MALE")
				.contacts(listOf("john_smith@gmail.com", "+1(555)555-55-55")).build();

		CustomerDto customer2 = CustomerDto.builder().id(2L)
				.credentials(new CredentialsDto("richard_faraway", "pass".toCharArray())).firstName("Richard")
				.lastName("Faraway").birthDate(LocalDate.of(1991, 2, 2)).sex("MALE")
				.contacts(listOf("richard_faraway@gmail.com", "+1(555)555-55-56")).build();

		CustomerDto customer3 = CustomerDto.builder().id(3L)
				.credentials(new CredentialsDto("joan_farwell", "pass".toCharArray())).firstName("Joan")
				.lastName("Farwell").birthDate(LocalDate.of(1992, 3, 3)).sex("FEMALE")
				.contacts(listOf("joan_farwell@gmail.com", "+1(555)555-55-57")).build();

		CountryDto england = new CountryDto(1L, "England");
		CountryDto us = new CountryDto(2L, "US");

		LanguageDto english = new LanguageDto(1L, "English");

		BookDto book1 = BookDto.builder().id(1L).author("Samuel Johnson").title("Those days").isbn("012345678")
				.publishDate(LocalDate.of(2015, 1, 1)).genre("FICTIONAL").country(england).language(english)
				.pageCount(100).size("QUARTO").coverType("HARD").coverSurface("UNCOATED").build();

		BookDto book2 = BookDto.builder().id(2L).author("Kate Yank").title("Chippendale fantasies").isbn("876543210")
				.publishDate(LocalDate.of(2014, 2, 2)).genre("PHILOSOPHICAL").country(us).language(english)
				.pageCount(200).size("FOLIO").coverType("HARD").coverSurface("GLOSS").build();

		BookDto book3 = BookDto.builder().id(3L).author("Troy Richter").title("Night and dawn").isbn("123456780")
				.publishDate(LocalDate.of(2013, 3, 3)).genre("HISTORICAL").country(us).language(english).pageCount(300)
				.size("DUODECIMO").coverType("SOFT").coverSurface("SILK").build();

		RequestDto request1 = RequestDto.builder().id(1L).createdTime(LocalDateTime.of(2020, 1, 1, 12, 0))
				.customer(customer1).build();
		request1.books().addAll(sortedSetOf(book1, book3));

		RequestDto request2 = RequestDto.builder().id(2L).createdTime(LocalDateTime.of(2021, 2, 2, 11, 15))
				.customer(customer2).build();
		request1.books().addAll(sortedSetOf(book2, book3));

		RequestDto request3 = RequestDto.builder().id(3L).createdTime(LocalDateTime.of(2022, 3, 3, 10, 9))
				.customer(customer3).build();
		request1.books().addAll(sortedSetOf(book1, book2));

		LibrarianDto librarian = LibrarianDto.builder().id(1L)
				.credentials(new CredentialsDto("vera_cruise", "pass".toCharArray())).firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex("FEMALE")
				.contacts(listOf("vera_cruise@gmail.com", "+1(555)555-05-55")).build();

		ApprovalDto approval1 = ApprovalDto.builder().id(1L).request(request1).librarian(librarian)
				.createdTime(LocalDateTime.of(2020, 4, 4, 6, 20)).build();
		approval1.books().addAll(sortedSetOf(book1, book3));

		ApprovalDto approval2 = ApprovalDto.builder().id(2L).request(request2).librarian(librarian)
				.createdTime(LocalDateTime.of(2021, 5, 5, 7, 30)).build();
		approval2.books().addAll(sortedSetOf(book2, book3));

		ApprovalDto approval3 = ApprovalDto.builder().id(3L).request(request3).librarian(librarian)
				.createdTime(LocalDateTime.of(2022, 6, 6, 8, 40)).build();
		approval3.books().addAll(sortedSetOf(book1, book2));

		Stream<ApprovalDto> approvedRequestsList = Stream.of(approval1, approval2, approval3);
		return approvedRequestsList;
	}

	private Streamable<Approval> prepareListOfApprovalEntities() {
		Customer customer1 = Customer.builder().id(1L).login("john_smith").passwordHash("pass").firstName("John")
				.lastName("Smith").birthDate(LocalDate.of(1990, 1, 1)).sex(Sex.MALE).build();
		customer1.getContacts()
				.addAll(sortedSetOf(Contact.create("john_smith@gmail.com"), Contact.create("+1(555)555-55-55")));

		Customer customer2 = Customer.builder().id(2L).login("richard_faraway").passwordHash("pass")
				.firstName("Richard").lastName("Faraway").birthDate(LocalDate.of(1991, 2, 2)).sex(Sex.MALE).build();
		customer2.getContacts()
				.addAll(sortedSetOf(Contact.create("richard_faraway@gmail.com"), Contact.create("+1(555)555-55-56")));

		Customer customer3 = Customer.builder().id(3L).login("joan_farwell").passwordHash("pass").firstName("Joan")
				.lastName("Farwell").birthDate(LocalDate.of(1992, 3, 3)).sex(Sex.FEMALE).build();
		customer3.getContacts()
				.addAll(sortedSetOf(Contact.create("joan_farwell@gmail.com"), Contact.create("+1(555)555-55-57")));

		Country england = new Country(1L, "England");
		Country us = new Country(2L, "US");

		Language english = new Language(1L, "English");
		Book book1 = Book.builder().id(1L).author("Samuel Johnson").title("Those days").isbn("012345678")
				.publishDate(LocalDate.of(2015, 1, 1)).genre(Genre.FICTIONAL).country(england).language(english)
				.pageCount(100).size(Size.QUARTO).cover(new Cover(Cover.Type.HARD, Cover.Surface.UNCOATED)).build();
		Book book2 = Book.builder().id(2L).author("Kate Yank").title("Chippendale fantasies").isbn("876543210")
				.publishDate(LocalDate.of(2014, 2, 2)).genre(Genre.PHILOSOPHICAL).country(us).language(english)
				.pageCount(200).size(Size.FOLIO).cover(new Cover(Cover.Type.HARD, Cover.Surface.GLOSS)).build();
		Book book3 = Book.builder().id(3L).author("Troy Richter").title("Night and dawn").isbn("123456780")
				.publishDate(LocalDate.of(2013, 3, 3)).genre(Genre.HISTORICAL).country(us).language(english)
				.pageCount(300).size(Size.DUODECIMO).cover(new Cover(Cover.Type.SOFT, Cover.Surface.SILK)).build();

		Request request1 = Request.builder().id(1L).createdTime(LocalDateTime.of(2020, 1, 1, 12, 0)).customer(customer1)
				.build();
		request1.getBooks().addAll(sortedSetOf(book1, book3));
		Request request2 = Request.builder().id(2L).createdTime(LocalDateTime.of(2021, 2, 2, 11, 15))
				.customer(customer2).build();
		request1.getBooks().addAll(sortedSetOf(book2, book3));
		Request request3 = Request.builder().id(3L).createdTime(LocalDateTime.of(2022, 3, 3, 10, 9)).customer(customer3)
				.build();
		request1.getBooks().addAll(sortedSetOf(book1, book2));

		Librarian librarian = Librarian.builder().id(1L).login("vera_cruise").passwordHash("pass").firstName("Vera")
				.lastName("Cruise").birthDate(LocalDate.of(1995, 11, 11)).sex(Sex.FEMALE).build();
		librarian.getContacts()
				.addAll(sortedSetOf(Contact.create("vera_cruise@gmail.com"), Contact.create("+1(555)555-05-55")));

		Approval approval1 = new Approval(1L, request1, librarian, LocalDateTime.of(2020, 4, 4, 6, 20));
		approval1.getBooks().addAll(sortedSetOf(book1, book3));
		Approval approval2 = new Approval(2L, request2, librarian, LocalDateTime.of(2021, 5, 5, 7, 30));
		approval2.getBooks().addAll(sortedSetOf(book2, book3));
		Approval approval3 = new Approval(3L, request3, librarian, LocalDateTime.of(2022, 6, 6, 8, 40));
		approval3.getBooks().addAll(sortedSetOf(book1, book2));

		Streamable<Approval> approvedRequestsList = Streamable.of(approval1, approval2, approval3);

		return approvedRequestsList;
	}

}
