package com.streamlined.library;

import java.util.Locale;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class LibraryApplication implements WebMvcConfigurer {

	public static final String LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER = "_selectedLanguage";
	public static final Locale DEFAULT_LOCALE = Locale.US;

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

	@Bean
	public LocaleResolver localeResolver() {
		var resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(DEFAULT_LOCALE);
		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		var localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName(LOCALE_CHANGE_INTERCEPTOR_LANGUAGE_PARAMETER);
		return localeChangeInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Bean
	public MessageSource messageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("language/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public MailSender mailSender(@Value("${spring.mail.host}") String mailHost,
			@Value("${spring.mail.port}") int mailPort, @Value("${spring.mail.username}") String userName,
			@Value("${spring.mail.password}") String password) {
		
		var mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(mailPort);
		mailSender.setUsername(userName);
		mailSender.setPassword(password);

		Properties properties = new Properties();
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.debug", "true");
		mailSender.setJavaMailProperties(properties);

		return mailSender;
	}

}
