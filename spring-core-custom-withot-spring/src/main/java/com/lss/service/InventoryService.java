package com.lss.service;

import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;
import com.lss.repository.ProductRepository;

public class InventoryService {
    private final ProductRepository productRepository;
    private final Logger logger;

    public InventoryService(ProductRepository productRepository, Logger logger) {
        this.productRepository = productRepository;
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public boolean checkAndReserve(String productId) {
        logger.log("Checking inventory for: " + productId);
        return productRepository.reserveProduct(productId);
    }
}
