package com.eventdrivenmicroservices.productservice.core.errorhandling;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

public class ProductServiceEventErrorHandler implements ListenerInvocationErrorHandler {
    @Override
    public void onError(
        Exception e, EventMessage<?> eventMessage, EventMessageHandler eventMessageHandler) throws Exception {
            throw e;
    }
}
