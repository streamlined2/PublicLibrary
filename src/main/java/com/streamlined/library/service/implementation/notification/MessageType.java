package com.streamlined.library.service.implementation.notification;

import java.util.Set;
import java.util.stream.Collectors;
import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Request;
import com.streamlined.library.model.Return;
import com.streamlined.library.model.Transfer;

public enum MessageType {

	NEW_CUSTOMER_CREATED("Successful registration", """
			Congratulations, %s!
			Your record was created successfully.
			Please visit our library, verify your personal data and proceed with catalog to order books.
			""") {

		@Override
		public String getFormattedText(Object... parameters) {
			Customer customer = (Customer) parameters[0];
			return text.formatted(customer.getFirstName());
		}

		@Override
		public Set<Long> getBookIds(Object... parameters) {
			return Set.of();
		}
	},

	REQUEST_RECEIVED("Request received", """
			Greetings, %s!
			Your request has been received and will be processed as soon as possible.
			Please check if all books you've been asking for were included:
			%s
			""") {

		@Override
		public String getFormattedText(Object... parameters) {
			Request request = (Request) parameters[0];
			return text.formatted(request.getCustomer().getFirstName(), request.getBooks().stream()
					.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
		}

		@Override
		public Set<Long> getBookIds(Object... parameters) {
			Request request = (Request) parameters[0];
			return request.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
		}
	},

	APPROVAL_RECEIVED("Approval received", """
			Greetings, %s!
			Your request has been approved for following books:
			%s
			""") {

		@Override
		public String getFormattedText(Object... parameters) {
			Approval approval = (Approval) parameters[0];
			return text.formatted(approval.getCustomer().getFirstName(), approval.getBooks().stream()
					.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
		}

		@Override
		public Set<Long> getBookIds(Object... parameters) {
			Approval approval = (Approval) parameters[0];
			return approval.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
		}
	},

	TRANSFER_ACCOMPLISHED("Transfer accomplished", """
			Greetings, %s!
			Following books were transferred to you:
			%s
			""") {

		@Override
		public String getFormattedText(Object... parameters) {
			Transfer transfer = (Transfer) parameters[0];
			return text.formatted(transfer.getCustomer().getFirstName(), transfer.getBooks().stream()
					.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
		}

		@Override
		public Set<Long> getBookIds(Object... parameters) {
			Transfer transfer = (Transfer) parameters[0];
			return transfer.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
		}
	},

	RETURN_ACCOMPLISHED("Return accomplished", """
			Greetings, %s!
			Following books were returned by you:
			%s
			""") {

		@Override
		public String getFormattedText(Object... parameters) {
			Return returnValue = (Return) parameters[0];
			return text.formatted(returnValue.getCustomer().getFirstName(), returnValue.getBooks().stream()
					.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
		}

		@Override
		public Set<Long> getBookIds(Object... parameters) {
			Return returnValue = (Return) parameters[0];
			return returnValue.getBooks().stream().map(Book::getId).collect(Collectors.toUnmodifiableSet());
		}
	};

	protected String topic;
	protected String text;

	MessageType(String topic, String text) {
		this.topic = topic;
		this.text = text;
	}

	public abstract String getFormattedText(Object... parameters);

	public abstract Set<Long> getBookIds(Object... parameters);

}
