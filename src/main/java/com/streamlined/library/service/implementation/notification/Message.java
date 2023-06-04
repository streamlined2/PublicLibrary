package com.streamlined.library.service.implementation.notification;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import com.streamlined.library.model.Customer;

public record Message(Customer customer, MessageType messageType, Object... parameters) {

	@Override
	public boolean equals(Object o) {
		if (o instanceof Message m) {
			return Objects.equals(customer, m.customer) && Objects.equals(messageType, m.messageType)
					&& Arrays.equals(parameters, m.parameters);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer, messageType, parameters);
	}

	@Override
	public String toString() {
		return "%s [%s] (%s)".formatted(customer.toString(), messageType.toString(), Arrays.toString(parameters));
	}

	public String getTopic() {
		return messageType.topic;
	}

	public String getFormattedText() {
		return messageType.getFormattedText(parameters);
	}

	public Set<Long> getBookIds() {
		return messageType.getBookIds(parameters);
	}

}
