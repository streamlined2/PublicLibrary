package com.streamlined.library;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import com.streamlined.library.dao.BookRepository;
import com.streamlined.library.dao.CountryRepository;
import com.streamlined.library.dao.LanguageRepository;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Country;
import com.streamlined.library.model.Language;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class LibraryApplication implements CommandLineRunner {

	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private BookRepository bookRepository;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("\nlist of countries:\n{}", StreamSupport.stream(countryRepository.findAll().spliterator(), false)
				.map(Country::toString).collect(Collectors.joining("\n")));
		log.info("\nlist of languages:\n{}", StreamSupport.stream(languageRepository.findAll().spliterator(), false)
				.map(Language::toString).collect(Collectors.joining("\n")));
		log.info("\nlist of books:\n{}",
				StreamSupport.stream(bookRepository.findAll(Sort.unsorted()).spliterator(), false).map(Book::toString)
						.collect(Collectors.joining("\n")));
	}

}
