package com.streamlined.library.service.implementation.notification.event;

import java.util.Set;
import java.util.stream.Collectors;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Request;

public class RequestReceivedEvent extends Event implements CustomerRelated {

	private final Request request;

	public RequestReceivedEvent(Request request) {
		this.request = request;
	}

	@Override
	public String getTopic() {
		return "Request received";
	}

	@Override
	public String getFormattedText() {
		return """
				Greetings, %s!
				Your request has been received and will be processed as soon as possible.
				Please check if all books you've been asking for were included:
				%s
				""".formatted(request.getCustomer().getFirstName(), request.getBooks().stream()
				.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
	}

	@Override
	public Set<Long> getBookIds() {
		return request.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public Customer getCustomer() {
		return request.getCustomer();
	}

	@Override
	public String toString() {
		return "request received: %s".formatted(request.toString());
	}

}
