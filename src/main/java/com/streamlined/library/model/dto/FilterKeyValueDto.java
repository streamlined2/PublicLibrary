package com.streamlined.library.model.dto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import java.util.Set;

import lombok.Data;

@Data
public class FilterKeyValueDto implements Iterable<Entry<String, String>> {

	private static final String FLAG_SUFFIX = "-flag";
	private static final String FLAG_VALUE_SELECTED = "on";

	private final Map<String, String> keyValues = new HashMap<>();
	private final Set<String> ignorePaths = new HashSet<>();

	public void setKeyValue(String key, String value) {
		keyValues.put(key, value);
		ignorePaths.remove(key);
	}

	public void clear() {
		keyValues.clear();
		ignorePaths.clear();
	}

	public void updateKeyValues(Map<String, String> parameters) {
		var selectedKeys = getSelectedKeys(parameters);
		ignorePaths.addAll(parameters.keySet());
		ignorePaths.removeAll(selectedKeys);
		for (var key : selectedKeys) {
			setKeyValue(key, parameters.get(key));
		}
	}

	private Set<String> getSelectedKeys(Map<String, String> parameters) {
		return parameters.entrySet().stream().filter(this::isKeySelected).map(this::getKeyName)
				.collect(Collectors.toSet());
	}

	private boolean isKeySelected(Entry<String, String> entry) {
		return entry.getKey().endsWith(FLAG_SUFFIX) && FLAG_VALUE_SELECTED.equals(entry.getValue());
	}

	private String getKeyName(Entry<String, String> entry) {
		String keyFlagName = entry.getKey();
		return keyFlagName.substring(0, keyFlagName.indexOf(FLAG_SUFFIX));
	}

	public Set<String> getIgnorePaths() {
		return Set.copyOf(ignorePaths);
	}

	@Override
	public Iterator<Entry<String, String>> iterator() {
		return Set.copyOf(keyValues.entrySet()).iterator();
	}

}
