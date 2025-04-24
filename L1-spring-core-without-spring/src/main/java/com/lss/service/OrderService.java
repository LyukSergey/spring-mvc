package com.lss.service;

import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;

public class OrderService {
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final NotificationService notificationService;
    private final Logger logger;

    public OrderService(PaymentService paymentService,
            InventoryService inventoryService,
            NotificationService notificationService,
            Logger logger) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public void placeOrder(String productId, String account, String email) {
        logger.log("Order placed for product " + productId);

        if (!inventoryService.checkAndReserve(productId)) {
            logger.log("Inventory check failed");
            return;
        }

        paymentService.pay(account, 100.0);
        notificationService.sendConfirmation(email);
    }
}
