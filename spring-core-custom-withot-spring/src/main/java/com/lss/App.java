package com.lss;

import com.lss.client.BankApiClient;
import com.lss.client.EmailClient;
import com.lss.logger.Logger;
import com.lss.repository.ProductRepository;
import com.lss.service.InventoryService;
import com.lss.service.NotificationService;
import com.lss.service.OrderService;
import com.lss.service.PaymentService;
import com.lss.utils.ObjectTracker;

public class App {

    public static void main(String[] args) {
        System.out.println("//-----User 1 order-----//");
        order("P123", "ACC456", "user1@example.com");
        System.out.println("//-----User 2 order-----//");
        order("P124", "ACC789", "user2@example.com");
        ObjectTracker.total();

    }

    private static void order(String productId, String account, String mail) {
        // Базовий singleton Logger
        Logger logger = new Logger();

        // Leaf об'єкти
        BankApiClient bankApiClient = new BankApiClient(logger);
        ProductRepository productRepository = new ProductRepository(logger);
        EmailClient emailClient = new EmailClient(logger);

        // Проміжні рівні
        PaymentService paymentService = new PaymentService(bankApiClient, logger);
        InventoryService inventoryService = new InventoryService(productRepository, logger);
        NotificationService notificationService = new NotificationService(emailClient, logger);

        // Top-level сервіс
        OrderService orderService = new OrderService(
                paymentService,
                inventoryService,
                notificationService,
                logger
        );

        // Виконуємо замовлення
        orderService.placeOrder(productId, account, mail);
    }
}

