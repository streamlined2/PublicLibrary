package com.streamlined.library;

import java.util.Arrays;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class LibraryApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Arrays.asList(context.getBeanDefinitionNames()).forEach(log::info);
		log.info("Total number of bean definitions {}", context.getBeanDefinitionCount());
		log.info("Logger implementation class {}", log.getClass().getName());
		log.info("Application startup date/time {}", new Date(context.getStartupDate()));
		log.info("DataSource class name {}", dataSource.getClass().getName());
		try (var connection = dataSource.getConnection()) {
			final var metaData = connection.getMetaData();
			log.info("Database product name {}, version {} ({}.{})", metaData.getDatabaseProductName(),
					metaData.getDatabaseProductVersion(), metaData.getDatabaseMajorVersion(),
					metaData.getDatabaseMinorVersion());
			log.info("Data source driver name {} and version {}.{}", metaData.getDriverName(),
					metaData.getDriverMajorVersion(), metaData.getDriverMinorVersion());
			log.info("Datasource URL {}, user name '{}' (max length {})", metaData.getURL(), metaData.getUserName(),
					metaData.getMaxUserNameLength());
			for (var schemas = metaData.getSchemas(); schemas.next();) {
				log.info("schema name {}", schemas.getObject(1));
			}
		}
	}

}
