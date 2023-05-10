package com.streamlined.library.model.dto.converter;

import javax.money.MonetaryAmount;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.streamlined.library.service.MonetaryService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StringToMonetaryAmountConverter implements Converter<String, MonetaryAmount> {

	private final MonetaryService monetaryService;

	@Override
	public MonetaryAmount convert(String source) {
		return monetaryService.parseValue(source);
	}

}
