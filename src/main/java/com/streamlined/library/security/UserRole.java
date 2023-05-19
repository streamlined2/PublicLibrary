package com.streamlined.library.security;

import java.util.Arrays;

import com.streamlined.library.WrongUserRoleException;

public enum UserRole {

	CUSTOMER("ROLE_CUSTOMER"), LIBRARIAN("ROLE_LIBRARIAN"), MANAGER("ROLE_MANAGER");

	private final String role;

	UserRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public static UserRole getRoleByName(String role) {
		return Arrays.stream(values()).filter(r -> r.getRole().equals(role)).findAny()
				.orElseThrow(() -> new WrongUserRoleException("no role %s defined".formatted(role)));
	}

}
