package com.streamlined.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "login")
public class CredentialsDto {

	private static final int MAX_PASSWORD_LENGTH = 25;

	private String login;
	@Builder.Default
	private char[] password = new char[MAX_PASSWORD_LENGTH];

	public CredentialsDto(String login) {
		this.login = login;
	}

}
