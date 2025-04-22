package com.lss.repository;

import com.lss.logger.Logger;
import com.lss.utils.ObjectTracker;

public class ProductRepository {
    private final Logger logger;

    public ProductRepository(Logger logger) {
        this.logger = logger;
        ObjectTracker.created(getClass());
    }

    public boolean reserveProduct(String productId) {
        logger.log("Reserving product: " + productId);
        return true;
    }

}
