package com.streamlined.library;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class LibraryApplication implements CommandLineRunner {
	
	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Arrays.asList(context.getBeanDefinitionNames()).forEach(log::info);
		log.info("Total number of bean definitions {}", context.getBeanDefinitionCount());
		log.info("Logger implementation class {}", log.getClass().getName());
		log.info("Application startup date/time {}", new Date(context.getStartupDate()));
	}

}
