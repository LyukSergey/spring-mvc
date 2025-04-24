package com.lss.prototype;

public class CarFactory {
    private final Car prototype;

    public CarFactory(Car prototype) {
        this.prototype = prototype;
    }

    public Car createClone() {
        return prototype.clone();
    }
}


