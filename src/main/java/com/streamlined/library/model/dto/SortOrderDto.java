package com.streamlined.library.model.dto;

import java.util.Optional;

import org.springframework.data.domain.Sort;

import com.streamlined.library.ParseException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SortOrderDto {

	private static final String DEFAULT_SORT_PROPERTY = "author";
	private static final String DEFAULT_SORT_ORDER = "asc";

	private String sort;
	private String order;

	public static SortOrderDto create() {
		return new SortOrderDto(DEFAULT_SORT_PROPERTY, DEFAULT_SORT_ORDER);
	}

	public static Sort.Order getOrderByParameter(Optional<SortOrderDto> order) {
		return order.map(SortOrderDto::mapToSortOrder).orElse(Sort.Order.asc("author"));
	}

	private static Sort.Order mapToSortOrder(SortOrderDto value) {
		if (value == null || value.getOrder() == null) {
			return Sort.Order.asc(DEFAULT_SORT_PROPERTY);
		}
		return switch (value.getOrder().toLowerCase()) {
		case "asc" -> Sort.Order.asc(value.getSort());
		case "desc" -> Sort.Order.desc(value.getSort());
		default -> throw new ParseException("incorrect sort order passed %s".formatted(value.getOrder()));
		};
	}

}
