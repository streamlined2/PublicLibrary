package com.streamlined.library.service.implementation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.streamlined.library.service.DateBoundaryService;

@Service
public class DefaultDateBoundaryService implements DateBoundaryService {

	private final List<Integer> yearBoundary = List.of(50, 30, 20, 10);

	public List<LocalDate> getDateBoundary() {
		LocalDate presentDate = LocalDate.now();
		var boundary = new LinkedList<LocalDate>();
		for (var year : yearBoundary) {
			boundary.add(presentDate.minus(year, ChronoUnit.YEARS));
		}
		return boundary;
	}

	public List<String> getDateBoundaryRepresentation() {
		var boundary = new LinkedList<String>();
		var iterator = yearBoundary.iterator();
		if (iterator.hasNext()) {
			var lower = iterator.next();
			boundary.add("older %d".formatted(lower));
			var higher = lower;
			while (iterator.hasNext()) {
				higher = iterator.next();
				boundary.add("between %d and %d".formatted(higher, lower));
				lower = higher;
			}
			boundary.add("younger %d".formatted(higher));
		}
		return boundary;
	}

}
