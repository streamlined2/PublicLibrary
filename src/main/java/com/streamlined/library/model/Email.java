package com.streamlined.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("2")
@NoArgsConstructor
@SuperBuilder
public class Email extends Contact {

	@NotNull
	@Column(name = "contact", nullable = false, unique = true)
	@jakarta.validation.constraints.Email
	public String getEmailAddress() {
		return contactInfo;
	}

	public void setEmailAddress(String emailAddress) {
		this.contactInfo = emailAddress;
	}

}
