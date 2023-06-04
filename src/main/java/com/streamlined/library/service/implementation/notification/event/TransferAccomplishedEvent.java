package com.streamlined.library.service.implementation.notification.event;

import java.util.Set;
import java.util.stream.Collectors;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Transfer;

public class TransferAccomplishedEvent extends Event implements CustomerRelated {

	private final Transfer transfer;

	public TransferAccomplishedEvent(Transfer transfer) {
		this.transfer = transfer;
	}

	@Override
	public String getTopic() {
		return "Transfer accomplished";
	}

	@Override
	public String getFormattedText() {
		return """
				Greetings, %s!
				Following books were transferred to you:
				%s
				""".formatted(transfer.getCustomer().getFirstName(), transfer.getBooks().stream()
				.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
	}

	@Override
	public Set<Long> getBookIds() {
		return transfer.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public Customer getCustomer() {
		return transfer.getCustomer();
	}

	@Override
	public String toString() {
		return "transfer accomplished: %s".formatted(transfer.toString());
	}

}
