package com.streamlined.library.service.implementation.notification.event;

import java.util.Set;
import java.util.stream.Collectors;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Return;

public class ReturnAccomplishedEvent extends Event implements CustomerRelated {

	private final Return returnValue;

	public ReturnAccomplishedEvent(Return returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public String getTopic() {
		return "Return accomplished";
	}

	@Override
	public String getFormattedText() {
		return """
				Greetings, %s!
				Following books were returned by you:
				%s
				""".formatted(returnValue.getCustomer().getFirstName(), returnValue.getBooks().stream()
				.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
	}

	@Override
	public Set<Long> getBookIds() {
		return returnValue.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public Customer getCustomer() {
		return returnValue.getCustomer();
	}

	@Override
	public String toString() {
		return "return accomplished: %s".formatted(returnValue.toString());
	}

}
