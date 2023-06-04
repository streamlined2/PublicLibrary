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
import com.streamlined.library.service.implementation.notification.Sender;
import com.streamlined.library.service.implementation.notification.event.ApprovalReceivedEvent;
import com.streamlined.library.service.implementation.notification.event.Event;
import com.streamlined.library.service.implementation.notification.event.NewCustomerCreatedEvent;
import com.streamlined.library.service.implementation.notification.event.RequestReceivedEvent;
import com.streamlined.library.service.implementation.notification.event.ReturnAccomplishedEvent;
import com.streamlined.library.service.implementation.notification.event.TransferAccomplishedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultNotificationService implements NotificationService {

	private static final long MESSAGE_DELIVERY_DELAY = 10_000L;

	private final BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>();

	private final List<Sender> senderList;

	@Override
	public void notifyNewCustomerRegistered(Customer customer) {
		eventQueue.add(new NewCustomerCreatedEvent(customer));
	}

	@Override
	public void notifyRequestReceived(Request request) {
		eventQueue.add(new RequestReceivedEvent(request));
	}

	@Override
	public void notifyApprovalReceived(Approval approval) {
		eventQueue.add(new ApprovalReceivedEvent(approval));
	}

	@Override
	public void notifyTransferAccomplished(Transfer transfer) {
		eventQueue.add(new TransferAccomplishedEvent(transfer));
	}

	@Override
	public void notifyReturnAccomplished(Return returnValue) {
		eventQueue.add(new ReturnAccomplishedEvent(returnValue));
	}

	@Scheduled(fixedDelay = MESSAGE_DELIVERY_DELAY)
	private void deliver() {
		for (Event event; (event = eventQueue.poll()) != null;) {
			send(event);
		}
	}

	private void send(Event event) {
		for (var sender : senderList) {
			if (sender.accepts(event.getClass())) {
				sender.send(event);
			}
		}
	}

}
