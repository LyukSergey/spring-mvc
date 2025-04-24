package com.lss.client;

import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;

public class EmailClient {
    private final Logger logger;

    public EmailClient(Logger logger) {
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public void send(String to, String message) {
        logger.log("Email sent to " + to + ": " + message);
    }
}
