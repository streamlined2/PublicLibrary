package com.streamlined.library;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.WebRequest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utilities {

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

}
