package com.streamlined.library.service.implementation;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.ToStringMonetaryAmountFormat;
import org.javamoney.moneta.ToStringMonetaryAmountFormat.ToStringMonetaryAmountFormatStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.streamlined.library.service.MonetaryService;

@Service
public class DefaultMonetaryService implements MonetaryService {

	private final String currencyCode;

	public DefaultMonetaryService(@Value("${currency}") String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@Override
	public CurrencyUnit getCurrencyUnit() {
		return Monetary.getCurrency(currencyCode);
	}

	@Override
	public Money getValue(Number value) {
		return Money.of(value, getCurrencyUnit());
	}
	
	@Override
	public MonetaryAmount parseValue(String value) {
		return ToStringMonetaryAmountFormat.of(ToStringMonetaryAmountFormatStyle.MONEY).parse(value);
	}

	@Override
	public String toString(MonetaryAmount amount) {
		return ToStringMonetaryAmountFormat.of(ToStringMonetaryAmountFormatStyle.MONEY).format(amount);
	}

}
