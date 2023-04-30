package com.streamlined.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.dao.ManagerRepository;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Librarian;
import com.streamlined.library.model.Manager;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class LibraryApplication {

	private final CustomerRepository customerRepository;
	private final LibrarianRepository librarianRepository;
	private final ManagerRepository managerRepository;

	private static final Long CUSTOMER_PRIMARY_KEY = 100L;
	private static final Long LIBRARIAN_PRIMARY_KEY = 2L;
	private static final Long MANAGER_PRIMARY_KEY = 3L;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf().disable();
		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
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

	@Bean
	public Manager manager() {// TODO must be replaced with authenticated user from security context
		return managerRepository.findById(MANAGER_PRIMARY_KEY).orElseThrow(
				() -> new NoEntityFoundException("no manager found with id %d".formatted(MANAGER_PRIMARY_KEY)));
	}

}
