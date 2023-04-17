package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Language;
import com.streamlined.library.model.dto.LanguageDto;

@Component
public class LanguageMapper implements Mapper<LanguageDto, Language> {

	public LanguageDto toDto(Language entity) {
		return LanguageDto.builder().id(entity.getId()).name(entity.getName()).build();
	}

	public Language toEntity(LanguageDto dto) {
		return Language.builder().id(dto.getId()).name(dto.getName()).build();
	}

}
