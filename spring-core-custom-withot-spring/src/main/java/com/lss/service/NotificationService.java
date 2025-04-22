package com.lss.service;

import com.lss.client.EmailClient;
import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;

public class NotificationService {
    private final EmailClient emailClient;
    private final Logger logger;

    public NotificationService(EmailClient emailClient, Logger logger) {
        this.emailClient = emailClient;
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public void sendConfirmation(String email) {
        logger.log("Sending confirmation to " + email);
        emailClient.send(email, "Your order was processed successfully.");
    }

}
