package com.streamlined.library.service.implementation.notification;

import com.streamlined.library.service.implementation.notification.event.Event;

public interface Sender {

	void send(Event event);

	boolean accepts(Class<? extends Event> eventClass);

}
