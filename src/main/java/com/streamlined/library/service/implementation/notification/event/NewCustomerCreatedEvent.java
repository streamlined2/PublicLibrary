package com.streamlined.library.service.implementation.notification.event;

import java.util.Set;

import com.streamlined.library.model.Customer;

public class NewCustomerCreatedEvent extends Event implements CustomerRelated {

	private final Customer customer;

	public NewCustomerCreatedEvent(Customer customer) {
		this.customer = customer;
	}

	@Override
	public String getTopic() {
		return "Successful registration";
	}

	@Override
	public String getFormattedText() {
		return """
				Congratulations, %s!
				Your record was created successfully.
				Please visit our library, verify your personal data and proceed with catalog to order books.
				""".formatted(customer.getFirstName());
	}

	@Override
	public Set<Long> getBookIds() {
		return Set.of();
	}

	@Override
	public Customer getCustomer() {
		return customer;
	}

	@Override
	public String toString() {
		return "new customer created: %s".formatted(customer.toString());
	}

}
