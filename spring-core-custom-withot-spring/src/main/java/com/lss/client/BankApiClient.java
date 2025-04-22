package com.lss.client;

import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;

public class BankApiClient {

    private final Logger logger;

    public BankApiClient(Logger logger) {
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public void processPayment(String account, double amount) {
        logger.log("Processing payment of $" + amount + " for account " + account);
    }

}
