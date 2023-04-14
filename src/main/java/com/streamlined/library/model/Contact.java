package com.streamlined.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.streamlined.library.ParseException;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Contact {

	@Id
	@Column(name = "contact", nullable = false, unique = true)
	protected @NotNull String contactInfo;

	public String getContactInfo() {
		return contactInfo;
	}

	public static Contact create(String contactInfo) {
		if (contactInfo.startsWith("+") && contactInfo.contains("(") && contactInfo.contains(")")) {
			return Phone.builder().contactInfo(contactInfo).build();
		}
		if (contactInfo.contains("@") && contactInfo.contains(".")) {
			return Email.builder().contactInfo(contactInfo).build();
		}
		throw new ParseException("'%s' cannot be recognized as valid contact info".formatted(contactInfo));
	}

}
