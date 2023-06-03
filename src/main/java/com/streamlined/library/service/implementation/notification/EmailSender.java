package com.streamlined.library.service.implementation.notification;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Email;

@Component
public class EmailSender implements Sender {

	private final MailSender mailSender;
	private final String userName;

	private EmailSender(MailSender mailSender, @Value("${spring.mail.username}") String userName) {
		this.mailSender = mailSender;
		this.userName = userName;
	}

	@Override
	public void send(Message message) {
		getEmailsFor(message.customer()).forEach(email -> sendMessage(email.getEmailAddress(), message));
	}

	private Stream<Email> getEmailsFor(Customer customer) {
		return customer.getContacts().stream().filter(Email.class::isInstance).map(Email.class::cast);
	}

	private void sendMessage(String email, Message message) {
		var mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(userName);
		mailMessage.setReplyTo(userName);
		mailMessage.setSubject(message.getTopic());
		mailMessage.setText(message.getFormattedText());
		mailMessage.setTo(email);
		mailSender.send(mailMessage);
	}

}
