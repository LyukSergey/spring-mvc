package com.lss;

import com.lss.prototype.Car;
import com.lss.prototype.CarFactory;
import com.lss.singlton.Singleton;

public class App {

    public static void main(String[] args) {
        final Singleton instance1 = Singleton.getInstance();
        final Singleton instance2 = Singleton.getInstance();
        System.out.printf("Singleton -> instance1 == instance2 %s%n", instance1 == instance2);

        Car toyotaPrototype = new Car("Toyota Supra", 2023);
        CarFactory factory = new CarFactory(toyotaPrototype);
        Car car1 = factory.createClone();
        Car car2 = factory.createClone();
        System.out.printf("Prototype -> car1 == car2 %s%n",  car1 == car2);
    }
}
