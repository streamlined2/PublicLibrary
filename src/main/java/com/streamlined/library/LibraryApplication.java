package com.streamlined.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Librarian;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class LibraryApplication {

	private final CustomerRepository customerRepository;
	private final LibrarianRepository librarianRepository;

	private static final Long CUSTOMER_PRIMARY_KEY = 1L;
	private static final Long LIBRARIAN_PRIMARY_KEY = 2L;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public Customer customer() {// TODO must be replaced with authenticated user from security context
		return customerRepository.findById(CUSTOMER_PRIMARY_KEY).orElseThrow(
				() -> new NoEntityFoundException("no customer with id %d".formatted(CUSTOMER_PRIMARY_KEY)));
	}

	@Bean
	public Librarian librarian() {// TODO must be replaced with authenticated user from security context
		return librarianRepository.findById(LIBRARIAN_PRIMARY_KEY).orElseThrow(
				() -> new NoEntityFoundException("no librarian with id %d".formatted(LIBRARIAN_PRIMARY_KEY)));
	}

}
