package com.streamlined.library.service.implementation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Email;
import com.streamlined.library.model.Request;
import com.streamlined.library.model.Return;
import com.streamlined.library.model.Transfer;
import com.streamlined.library.service.NotificationService;

@Service
public class DefaultNotificationService implements NotificationService {

	private static final long MESSAGE_DELIVERY_DELAY = 10_000L;

	private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

	private final MailSender mailSender;
	private final String userName;

	private DefaultNotificationService(MailSender mailSender, @Value("${spring.mail.username}") String userName) {
		this.mailSender = mailSender;
		this.userName = userName;
	}

	@Override
	public void notifyNewCustomerRegistered(Customer customer) {
		addMessagesToQueue(customer, MessageType.NEW_CUSTOMER_CREATED, customer);
	}

	@Override
	public void notifyRequestReceived(Request request) {
		addMessagesToQueue(request.getCustomer(), MessageType.REQUEST_RECEIVED, request);
	}

	@Override
	public void notifyApprovalReceived(Approval approval) {
		addMessagesToQueue(approval.getCustomer(), MessageType.APPROVAL_RECEIVED, approval);
	}

	@Override
	public void notifyTransferAccomplished(Transfer transfer) {
		addMessagesToQueue(transfer.getCustomer(), MessageType.TRANSFER_ACCOMPLISHED, transfer);
	}

	@Override
	public void notifyReturnAccomplished(Return returnValue) {
		addMessagesToQueue(returnValue.getCustomer(), MessageType.RETURN_ACCOMPLISHED, returnValue);
	}

	private void addMessagesToQueue(Customer customer, MessageType messageType, Object... parameters) {
		getEmailsFor(customer).forEach(email -> messageQueue.add(
				new Message(email.getEmailAddress(), messageType.topic, messageType.getFormattedText(parameters))));
	}

	private Stream<Email> getEmailsFor(Customer customer) {
		return customer.getContacts().stream().filter(Email.class::isInstance).map(Email.class::cast);
	}

	@Scheduled(fixedDelay = MESSAGE_DELIVERY_DELAY)
	private void deliverMessages() {
		for (Message event; (event = messageQueue.poll()) != null;) {
			sendMessage(event);
		}
	}

	private void sendMessage(Message event) {
		var mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(userName);
		mailMessage.setReplyTo(userName);
		mailMessage.setSubject(event.topic);
		mailMessage.setText(event.message);
		mailMessage.setTo(event.email);
		mailSender.send(mailMessage);
	}

	private record Message(String email, String topic, String message) {
	}

	private enum MessageType {
		NEW_CUSTOMER_CREATED("Successful registration", """
				Congratulations, %s!
				Your record was created successfully.
				Please visit our library, verify your personal data and proceed with catalog to order books.
				""") {

			@Override
			String getFormattedText(Object... parameters) {
				Customer customer = (Customer) parameters[0];
				return text.formatted(customer.getFirstName());
			}
		},

		REQUEST_RECEIVED("Request received", """
				Greetings, %s!
				Your request has been received and will be processed as soon as possible.
				Please check if all books you've been asking for were included:
				%s
				""") {

			@Override
			String getFormattedText(Object... parameters) {
				Request request = (Request) parameters[0];
				return text.formatted(request.getCustomer().getFirstName(), request.getBooks().stream()
						.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
			}
		},

		APPROVAL_RECEIVED("Approval received", """
				Greetings, %s!
				Your request has been approved for following books:
				%s
				""") {

			@Override
			String getFormattedText(Object... parameters) {
				Approval approval = (Approval) parameters[0];
				return text.formatted(approval.getCustomer().getFirstName(), approval.getBooks().stream()
						.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
			}
		},

		TRANSFER_ACCOMPLISHED("Transfer accomplished", """
				Greetings, %s!
				Following books were transferred to you:
				%s
				""") {

			@Override
			String getFormattedText(Object... parameters) {
				Transfer transfer = (Transfer) parameters[0];
				return text.formatted(transfer.getCustomer().getFirstName(), transfer.getBooks().stream()
						.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
			}
		},

		RETURN_ACCOMPLISHED("Return accomplished", """
				Greetings, %s!
				Following books were returned by you:
				%s
				""") {

			@Override
			String getFormattedText(Object... parameters) {
				Return returnValue = (Return) parameters[0];
				return text.formatted(returnValue.getCustomer().getFirstName(), returnValue.getBooks().stream()
						.map(Book::getAuthorTitlePublishYear).collect(Collectors.joining("\n\t", "\t", "\n")));
			}
		};

		protected String topic;
		protected String text;

		MessageType(String topic, String text) {
			this.topic = topic;
			this.text = text;
		}

		abstract String getFormattedText(Object... parameters);

	}

}
