package com.streamlined.library.service.implementation.notification.event;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Event {

	private static final AtomicLong ID_GENERATOR = new AtomicLong(Long.MIN_VALUE);

	private final long id;

	protected Event() {
		id = ID_GENERATOR.getAndIncrement();
	}

	public abstract String getTopic();

	public abstract String getFormattedText();

	public abstract Set<Long> getBookIds();

	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Event event) {
			return id == event.id;
		}
		return false;
	}

}
