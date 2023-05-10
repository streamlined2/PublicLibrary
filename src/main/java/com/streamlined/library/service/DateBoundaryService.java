package com.streamlined.library.service;

import java.time.LocalDate;
import java.util.List;

public interface DateBoundaryService {

	List<LocalDate> getDateBoundary();

	List<String> getDateBoundaryRepresentation();

}
