package com.streamlined.library.service;

import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Request;
import com.streamlined.library.model.Return;
import com.streamlined.library.model.Transfer;

public interface NotificationService {

	void notifyNewCustomerRegistered(Customer customer);

	void notifyRequestReceived(Request request);

	void notifyApprovalReceived(Approval approval);

	void notifyTransferAccomplished(Transfer transfer);

	void notifyReturnAccomplished(Return returnValue);

}
