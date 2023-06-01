package com.streamlined.library.service;

import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Request;

public interface NotificationService {

	void notifyNewCustomerRegistered(Customer customer);

	void notifyRequestReceived(Request request);

}
