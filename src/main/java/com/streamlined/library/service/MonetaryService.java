package com.streamlined.library.service;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

public interface MonetaryService {

	CurrencyUnit getCurrencyUnit();

	Money getValue(Number value);

	MonetaryAmount parseValue(String value);

	String toString(MonetaryAmount amount);

}
