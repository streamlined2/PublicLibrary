package com.streamlined.library.model.dto.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.streamlined.library.ParseException;
import com.streamlined.library.model.dto.CountryDto;
import static com.streamlined.library.Utilities.parseDtoParameterValues;

import java.util.Map;

@Component
public class StringToCountryDtoConverter implements Converter<String, CountryDto> {

	private static final String ID_KEY = "id";
	private static final String NAME_KEY = "name";

	@Override
	public CountryDto convert(String source) {
		Map<String, String> paramValues = parseDtoParameterValues(source);
		var builder = CountryDto.builder();
		if (!paramValues.containsKey(ID_KEY))
			throw new ParseException("no value for id parameter found");
		builder.id(Long.valueOf(paramValues.get(ID_KEY)));
		if (!paramValues.containsKey(NAME_KEY))
			throw new ParseException("no value for name parameter found");
		builder.name(paramValues.get(NAME_KEY));
		return builder.build();
	}

}
