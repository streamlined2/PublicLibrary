package com.streamlined.library;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

	private static final String USERS_BY_USERNAME_QUERY = """
			select u.login, u.password_hash, 1 from (
			 select c.login, c.password_hash from customer c
			 union all
			 select l.login, l.password_hash from librarian l
			 union all
			 select m.login, m.password_hash from manager m
			) as u
			where u.login = ?
			""";
	private static final String AUTHORITIES_BY_USERNAME_QUERY = """
			select u.login, u.role from (
			 select c.login, 'ROLE_CUSTOMER' as role from customer c
			 union all
			 select l.login, 'ROLE_LIBRARIAN' as role from librarian l
			 union all
			 select m.login, 'ROLE_MANAGER' as role from manager m
			) as u
			where u.login = ?
			""";

	private final DataSource dataSource;

	@Bean
	public UserDetailsService userDetailsService() {
		var manager = new JdbcUserDetailsManager(dataSource);
		manager.setUsersByUsernameQuery(USERS_BY_USERNAME_QUERY);
		manager.setAuthoritiesByUsernameQuery(AUTHORITIES_BY_USERNAME_QUERY);
		return manager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/index.html", "/user/checkin").permitAll()
				.anyRequest().authenticated());
		httpSecurity.csrf().disable();
		httpSecurity.formLogin(formLogin -> formLogin.loginPage("/user/checkin").loginProcessingUrl("/user/do-login")
				.usernameParameter("login").passwordParameter("password").defaultSuccessUrl("/")
				.failureUrl("/user/checkin?error=true"));
		httpSecurity.logout().logoutUrl("/user/checkout").logoutSuccessUrl("/").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID");
		httpSecurity.headers().frameOptions().sameOrigin();
		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		var encoders = new HashMap<String, PasswordEncoder>();
		encoders.put("bcrypt", new BCryptPasswordEncoder(BCryptVersion.$2Y));
		encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
		encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
		return new DelegatingPasswordEncoder("bcrypt", encoders);
	}

}
