package com.streamlined.library.service;

import com.streamlined.library.model.Customer;

public interface NotificationService {

	void notifyNewCustomerRegistered(Customer customer);

}
