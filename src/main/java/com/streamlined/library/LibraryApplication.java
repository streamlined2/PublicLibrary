package com.streamlined.library;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.model.Customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class LibraryApplication implements CommandLineRunner {

	private final CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {//TODO remove after check is complete
		log.info("Customer info:");
		for (var customer : customerRepository.findAll()) {
			log.info("customer #1 class {}, data {}", customer.getClass(), customer);
			for (var contact : customer.getContacts()) {
				log.info("contact list: class {}, data {}", contact.getClass(), contact);
			}
		}
	}

	@Bean
	public Customer customer() {
		return customerRepository.findById(1L).orElseThrow();
	}

}
