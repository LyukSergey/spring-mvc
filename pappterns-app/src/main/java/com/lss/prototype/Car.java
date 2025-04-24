package com.lss.prototype;

public class Car implements Prototype<Car> {
    private String model;
    private int year;

    public Car(String model, int year) {
        this.model = model;
        this.year = year;
    }

    @Override
    public Car clone() {
        return new Car(this.model, this.year);
    }

}


