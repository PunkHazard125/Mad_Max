package com.madmax.Simulation;

import com.madmax.Models.Vehicle;

import java.util.Random;

public class FuelBonusEvent extends Event {

    private final Random rand = new Random();

    public FuelBonusEvent(double probability) {
        super(probability);
    }

    @Override
    public boolean apply(Vehicle vehicle) {

        int fuel = vehicle.getFuel();
        int bonus = Math.max(1, (int)(fuel * (0.05 + (rand.nextDouble() * 0.10))));
        vehicle.refuel(bonus);

        vehicle.addEventCount();

        return false;

    }
}
