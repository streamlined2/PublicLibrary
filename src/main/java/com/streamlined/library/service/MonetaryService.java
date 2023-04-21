package com.streamlined.library.service;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.ToStringMonetaryAmountFormat;
import org.javamoney.moneta.ToStringMonetaryAmountFormat.ToStringMonetaryAmountFormatStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MonetaryService {

	private final String currencyCode;

	public MonetaryService(@Value("${currency}") String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public CurrencyUnit getCurrencyUnit() {
		return Monetary.getCurrency(currencyCode);
	}

	public Money getValue(Number value) {
		return Money.of(value, getCurrencyUnit());
	}
	
	public MonetaryAmount parseValue(String value) {
		return ToStringMonetaryAmountFormat.of(ToStringMonetaryAmountFormatStyle.MONEY).parse(value);
	}

	public String toString(MonetaryAmount amount) {
		return ToStringMonetaryAmountFormat.of(ToStringMonetaryAmountFormatStyle.MONEY).format(amount);
	}

}
