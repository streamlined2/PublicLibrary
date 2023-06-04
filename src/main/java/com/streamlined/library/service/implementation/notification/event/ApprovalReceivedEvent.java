package com.streamlined.library.service.implementation.notification.event;

import java.util.Set;
import java.util.stream.Collectors;

import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;

public class ApprovalReceivedEvent extends Event implements CustomerRelated {

	private final Approval approval;

	public ApprovalReceivedEvent(Approval approval) {
		this.approval = approval;
	}

	@Override
	public String getTopic() {
		return "Approval received";
	}

	@Override
	public String getFormattedText() {
		return """
				Greetings, %s!
				Your request has been approved for following books:
				%s
				""".formatted(approval.getCustomer().getFirstName(), approval.getBooks().stream()
				.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
	}

	@Override
	public Set<Long> getBookIds() {
		return approval.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
	}

	@Override
	public Customer getCustomer() {
		return approval.getCustomer();
	}

	@Override
	public String toString() {
		return "approval received: %s".formatted(approval.toString());
	}

}
