package com.streamlined.library;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

	@Bean
	public UserDetailsManager userDetailsManager() {
		UserDetails user = User.withUsername("simple_user").password("simple_password")
				.passwordEncoder(passwordEncoder()::encode).roles("USER").build();
		UserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(user);
		return manager;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(
				auth -> auth.requestMatchers("/", "/user/checkin").permitAll().anyRequest().authenticated());
		httpSecurity.csrf().disable();
		httpSecurity.formLogin(formLogin -> formLogin.loginPage("/user/checkin").loginProcessingUrl("/user/do-login")
				.usernameParameter("login").passwordParameter("password").defaultSuccessUrl("/")
				.failureUrl("/user/checkin?error=true"));
		httpSecurity.logout().logoutUrl("/user/checkout").logoutSuccessUrl("/");
		return httpSecurity.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
