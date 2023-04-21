package com.streamlined.library.model.dto.converter;

import javax.money.MonetaryAmount;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.streamlined.library.service.MonetaryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MonetaryAmountToStringConverter implements Converter<MonetaryAmount, String> {

	private final MonetaryService monetaryService;

	@Override
	public String convert(MonetaryAmount source) {
		return monetaryService.toString(source);
	}

}
