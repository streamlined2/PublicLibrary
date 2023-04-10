package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Language;
import com.streamlined.library.model.dto.LanguageDto;

@Component
public class LanguageMapper {

	public LanguageDto toDto(Language language) {
		return LanguageDto.builder().id(language.getId()).name(language.getName()).build();
	}

	public Language toEntity(LanguageDto dto) {
		return Language.builder().id(dto.getId()).name(dto.getName()).build();
	}

}
