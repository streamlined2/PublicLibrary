package com.streamlined.library;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilities {

	private static final String FLAG_SUFFIX = "-flag";
	private static final String FLAG_VALUE_SELECTED = "on";

	public String convertNanosecondsToDaysHoursString(BigDecimal durationNanoseconds) {
		var duration = Duration.of(durationNanoseconds.longValue(), ChronoUnit.NANOS);
		return "%2d day(s), %2d hour(s)".formatted(duration.toDays(), duration.toHoursPart());
	}

	public Map<String, String> parseDtoParameterValues(String dtoValue) {
		int leftParenthesisIndex = dtoValue.indexOf('(');
		if (leftParenthesisIndex < 0) {
			throw new ParseException("left parenthesis not found in source value %s".formatted(dtoValue));
		}
		int rightParenthesisIndex = dtoValue.lastIndexOf(')');
		if (rightParenthesisIndex < 0) {
			throw new ParseException("right parenthesis not found in source value %s".formatted(dtoValue));
		}
		if (leftParenthesisIndex >= rightParenthesisIndex) {
			throw new ParseException(
					"right parenthesis should follow left parenthesis in source value %s".formatted(dtoValue));
		}
		Map<String, String> parameterValues = new HashMap<>();
		String[] parameterValuePairs = dtoValue.substring(leftParenthesisIndex + 1, rightParenthesisIndex)
				.split(",\\s*");
		for (String parameterValueString : parameterValuePairs) {
			String[] parameterValue = getParameterValue(parameterValueString);
			parameterValues.put(parameterValue[0], parameterValue[1]);
		}
		return parameterValues;
	}

	public String[] getParameterValue(String parameterValueString) {
		String[] parameterValue = parameterValueString.split("=");
		if (parameterValue.length != 2) {
			throw new ParseException(
					"string %s should consist of parameter and value divided by =".formatted(parameterValueString));
		}
		return parameterValue;
	}

	public URI getSourceURI(WebRequest request) {
		return URI.create(getParameterValue(request.getDescription(false))[1]);
	}

	public String getSourceForm(WebRequest request) {
		var referer = request.getHeader(HttpHeaders.REFERER);
		if (referer == null) {
			throw new WrongRequestParameterException(
					"no referer header specified for URI %s".formatted(getSourceURI(request).toString()));
		}
		var path = URI.create(referer).getPath();
		var context = request.getContextPath();
		var contextStartIndex = path.indexOf(context);
		if (contextStartIndex == -1) {
			throw new WrongRequestParameterException(
					"not found context path %s in referer %s".formatted(context, referer));
		}
		return path.substring(contextStartIndex + context.length());
	}

	public List<Long> getBookIdList(Map<String, String> bookIds) {
		return bookIds.values().stream().map(Long::valueOf).toList();
	}

	public Sort.Order getOrderByParameter(String property, String order) {
		return switch (order.toLowerCase()) {
		case "asc" -> Sort.Order.asc(property);
		case "desc" -> Sort.Order.desc(property);
		default -> throw new ParseException("incorrect sort order passed %s".formatted(order));
		};
	}

	public Map<String, String> getFilterKeyValues(Map<String, String> parameters) {
		var keyValues = new HashMap<String, String>();
		for (var key : getSelectedKeys(parameters)) {
			keyValues.put(key, parameters.get(key));
		}
		return keyValues;
	}

	private Set<String> getSelectedKeys(Map<String, String> parameters) {
		return parameters.entrySet().stream().filter(Utilities::isKeySelected).map(Utilities::getKeyName)
				.collect(Collectors.toSet());
	}

	private boolean isKeySelected(Entry<String, String> entry) {
		return entry.getKey().endsWith(FLAG_SUFFIX) && FLAG_VALUE_SELECTED.equals(entry.getValue());
	}

	private String getKeyName(Entry<String, String> entry) {
		String keyFlagName = entry.getKey();
		return keyFlagName.substring(0, keyFlagName.indexOf(FLAG_SUFFIX));
	}

	public Set<String> getIgnorePaths(Map<String, String> parameters) {
		var ignorePaths = new HashSet<String>(parameters.keySet());
		ignorePaths.removeAll(getSelectedKeys(parameters));
		return ignorePaths;
	}

}
