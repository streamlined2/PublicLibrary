package com.streamlined.library.service.implementation;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Request;
import com.streamlined.library.model.Return;
import com.streamlined.library.model.Transfer;
import com.streamlined.library.service.NotificationService;
import com.streamlined.library.service.implementation.notification.Message;
import com.streamlined.library.service.implementation.notification.MessageType;
import com.streamlined.library.service.implementation.notification.Sender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultNotificationService implements NotificationService {

	private static final long MESSAGE_DELIVERY_DELAY = 10_000L;

	private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

	private final List<Sender> senderList;

	@Override
	public void notifyNewCustomerRegistered(Customer customer) {
		addMessageToQueue(customer, MessageType.NEW_CUSTOMER_CREATED, customer);
	}

	@Override
	public void notifyRequestReceived(Request request) {
		addMessageToQueue(request.getCustomer(), MessageType.REQUEST_RECEIVED, request);
	}

	@Override
	public void notifyApprovalReceived(Approval approval) {
		addMessageToQueue(approval.getCustomer(), MessageType.APPROVAL_RECEIVED, approval);
	}

	@Override
	public void notifyTransferAccomplished(Transfer transfer) {
		addMessageToQueue(transfer.getCustomer(), MessageType.TRANSFER_ACCOMPLISHED, transfer);
	}

	@Override
	public void notifyReturnAccomplished(Return returnValue) {
		addMessageToQueue(returnValue.getCustomer(), MessageType.RETURN_ACCOMPLISHED, returnValue);
	}

	private void addMessageToQueue(Customer customer, MessageType messageType, Object... parameters) {
		messageQueue.add(new Message(customer, messageType, parameters));
	}

	@Scheduled(fixedDelay = MESSAGE_DELIVERY_DELAY)
	private void deliverMessages() {
		for (Message message; (message = messageQueue.poll()) != null;) {
			sendMessage(message);
		}
	}

	private void sendMessage(Message message) {
		for (var sender : senderList) {
			sender.send(message);
		}
	}

}
