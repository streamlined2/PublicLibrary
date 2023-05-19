package com.streamlined.library;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.streamlined.library.controller.NoEntityFoundException;
import com.streamlined.library.dao.CustomerRepository;
import com.streamlined.library.dao.LibrarianRepository;
import com.streamlined.library.dao.ManagerRepository;
import com.streamlined.library.model.Person;
import com.streamlined.library.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("auditorAware")
@RequiredArgsConstructor
@Slf4j
public class ActivePrincipalAuditorAware implements AuditorAware<Person> {

	private final CustomerRepository customerRepository;
	private final LibrarianRepository librarianRepository;
	private final ManagerRepository managerRepository;

	@Override
	public Optional<Person> getCurrentAuditor() {
		log.debug("current user auditor started...");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !auth.isAuthenticated()) {
			return Optional.empty();
		}
		var login = auth.getName();
		var roles = auth.getAuthorities();
		if (roles.isEmpty()) {
			log.error("no role defined for user {}", login);
			throw new WrongUserRoleException("no role defined for user %s".formatted(login));
		}
		var role = roles.iterator().next().getAuthority();
		return getUserIdForRole(role, login);
	}

	private Optional<Person> getUserIdForRole(String role, String login) {
		var user = switch (UserRole.getRoleByName(role)) {
		case CUSTOMER -> customerRepository.findByLogin(login);
		case LIBRARIAN -> librarianRepository.findByLogin(login);
		case MANAGER -> managerRepository.findByLogin(login);
		default -> {
			log.error("wrong user role {}", role);
			throw new WrongUserRoleException("wrong user role %s".formatted(role));
		}
		};
		if (user.isEmpty()) {
			log.error("no user found with login {}", login);
			throw new NoEntityFoundException("no user found with login %s".formatted(login));
		}
		return Optional.of(user.get());
	}

}
