package com.streamlined.library.model.dto.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.streamlined.library.ParseException;
import com.streamlined.library.model.dto.LanguageDto;

import static com.streamlined.library.Utilities.parseDtoParameterValues;

import java.util.Map;

@Component
public class StringToLanguageDtoConverter implements Converter<String, LanguageDto> {

	private static final String ID_KEY = "id";
	private static final String NAME_KEY = "name";

	@Override
	public LanguageDto convert(String source) {
		Map<String, String> paramValues = parseDtoParameterValues(source);
		var builder = LanguageDto.builder();
		if (!paramValues.containsKey(ID_KEY))
			throw new ParseException("no value for id parameter found");
		builder.id(Long.valueOf(paramValues.get(ID_KEY)));
		if (!paramValues.containsKey(NAME_KEY))
			throw new ParseException("no value for name parameter found");
		builder.name(paramValues.get(NAME_KEY));
		return builder.build();
	}

}
