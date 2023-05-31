package com.streamlined.library.service.implementation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Email;
import com.streamlined.library.service.NotificationService;

@Service
public class DefaultNotificationService implements NotificationService {

	private static final long MESSAGE_DELIVERY_DELAY = 10_000L;

	private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

	private final MailSender mailSender;
	private final String userName;

	private DefaultNotificationService(MailSender mailSender, @Value("${spring.mail.username}") String userName) {
		this.mailSender = mailSender;
		this.userName = userName;
	}

	@Override
	public void notifyNewCustomerRegistered(Customer customer) {
		getEmailsFor(customer).forEach(email -> eventQueue.add(new Event(email, EventType.NEW_CUSTOMER_CREATED.topic,
				EventType.NEW_CUSTOMER_CREATED.message.formatted(customer.getFirstName()))));
	}

	@Scheduled(fixedDelay = MESSAGE_DELIVERY_DELAY)
	private void deliverMessages() {
		for (Event event; (event = eventQueue.poll()) != null;) {
			sendNotification(event);
		}
	}

	private void sendNotification(Event event) {
		var mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(userName);
		mailMessage.setReplyTo(userName);
		mailMessage.setSubject(event.topic);
		mailMessage.setText(event.message);
		mailMessage.setTo(event.email.getEmailAddress());
		mailSender.send(mailMessage);
	}

	private Stream<Email> getEmailsFor(Customer customer) {
		return customer.getContacts().stream().filter(Email.class::isInstance).map(Email.class::cast);
	}

	private record Event(Email email, String topic, String message) {
	}

	private enum EventType {
		NEW_CUSTOMER_CREATED("Successful registration", """
				Congratulations, %s!
				Your record was created successfully.
				Please visit our library, verify your personal data and proceed with catalog to order books.
				""");

		private String topic;
		private String message;

		EventType(String topic, String message) {
			this.topic = topic;
			this.message = message;
		}

	}

}
