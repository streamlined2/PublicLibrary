package com.streamlined.library.model.dto;

import com.streamlined.library.model.Language;

public record LanguageDto(Long id, String name) {

	public LanguageDto(Language language) {
		this(language.getId(), language.getName());
	}

	public Language getEntity() {
		return Language.builder().id(id).name(name).build();
	}

}
