package com.eventdrivenmicroservices.edmuserservice.query.event;

import com.eventdrivenmicroservices.edmcore.model.PaymentDetails;
import com.eventdrivenmicroservices.edmcore.model.User;
import com.eventdrivenmicroservices.edmcore.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventHandler {

    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
            .cardNumber("123Card")
            .cvv("123")
            .name("Joel Silva")
            .validUntilMonth(12)
            .validUntilYear(2030)
            .build();

        User user = User.builder()
            .firstName("Joel")
            .lastName("Silva")
            .userId(query.getUserId())
            .paymentDetails(paymentDetails)
            .build();

        return user;
    }

}
