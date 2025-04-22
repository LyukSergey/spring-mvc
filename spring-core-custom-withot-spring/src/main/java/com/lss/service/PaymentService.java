package com.lss.service;

import com.lss.client.BankApiClient;
import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;

public class PaymentService {
    private final BankApiClient bankApiClient;
    private final Logger logger;

    public PaymentService(BankApiClient bankApiClient, Logger logger) {
        this.bankApiClient = bankApiClient;
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public void pay(String account, double amount) {
        logger.log("Initiating payment...");
        bankApiClient.processPayment(account, amount);
    }

}
