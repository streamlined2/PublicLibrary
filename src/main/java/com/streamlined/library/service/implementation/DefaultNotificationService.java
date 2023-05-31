package com.streamlined.library.service.implementation;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Email;
import com.streamlined.library.service.NotificationService;

@Service
public class DefaultNotificationService implements NotificationService {

	private final MailSender mailSender;
	private final String userName;

	private DefaultNotificationService(MailSender mailSender, @Value("${spring.mail.username}") String userName) {
		this.mailSender = mailSender;
		this.userName = userName;
	}

	@Override
	public void notifyNewCustomerRegistered(Customer customer) {
		getEmailsFor(customer).forEach(email -> sendNotification(email, Event.NEW_CUSTOMER_CREATED.topic,
				Event.NEW_CUSTOMER_CREATED.message.formatted(customer.getFirstName())));
	}

	private void sendNotification(Email email, String subject, String messageText) {
		var mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(userName);
		mailMessage.setReplyTo(userName);
		mailMessage.setSubject(subject);
		mailMessage.setText(messageText);
		mailMessage.setTo(email.getEmailAddress());
		mailSender.send(mailMessage);
	}

	private Stream<Email> getEmailsFor(Customer customer) {
		return customer.getContacts().stream().filter(Email.class::isInstance).map(Email.class::cast);
	}

	private enum Event {
		NEW_CUSTOMER_CREATED("Successful registration", """
				Congratulations, %s!
				Your record was created successfully.
				Please visit our library, verify your personal data and proceed with catalog to order books.
				""");

		private String topic;
		private String message;

		Event(String topic, String message) {
			this.topic = topic;
			this.message = message;
		}

	}

}
