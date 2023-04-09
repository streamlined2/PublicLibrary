package com.streamlined.library.model.dto;

import com.streamlined.library.model.Language;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageDto {

	private Long id;
	private String name;

	public static LanguageDto create(Language language) {
		return builder().id(language.getId()).name(language.getName()).build();
	}

	public Language getEntity() {
		return Language.builder().id(id).name(name).build();
	}

}
