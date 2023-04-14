package com.streamlined.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("1")
@NoArgsConstructor
@SuperBuilder
public class Phone extends Contact {

	@NotNull
	@Column(name = "contact", nullable = false, unique = true)
	@Pattern(regexp = "\\+\\d{1,3}\\(\\d{1,3}\\)\\d{3}-\\d{2}-\\d{2}")
	public String getPhoneNumber() {
		return contactInfo;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.contactInfo = phoneNumber;
	}

}
