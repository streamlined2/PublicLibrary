package com.streamlined.library.service.implementation.notification;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Email;
import com.streamlined.library.service.implementation.notification.event.CustomerRelated;
import com.streamlined.library.service.implementation.notification.event.Event;

@Component
public class EmailSender implements Sender {

	private final MailSender mailSender;
	private final String userName;

	private EmailSender(MailSender mailSender, @Value("${spring.mail.username}") String userName) {
		this.mailSender = mailSender;
		this.userName = userName;
	}

	@Override
	public void send(Event event) {
		getEmailsFor(((CustomerRelated) event).getCustomer())
				.forEach(email -> sendMessage(email.getEmailAddress(), event));
	}

	@Override
	public boolean accepts(Class<? extends Event> eventClass) {
		return CustomerRelated.class.isAssignableFrom(eventClass);
	}

	private Stream<Email> getEmailsFor(Customer customer) {
		return customer.getContacts().stream().filter(Email.class::isInstance).map(Email.class::cast);
	}

	private void sendMessage(String email, Event event) {
		var mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(userName);
		mailMessage.setReplyTo(userName);
		mailMessage.setSubject(event.getTopic());
		mailMessage.setText(event.getFormattedText());
		mailMessage.setTo(email);
		mailSender.send(mailMessage);
	}

}
