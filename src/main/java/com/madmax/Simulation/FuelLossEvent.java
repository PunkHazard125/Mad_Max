package com.madmax.Simulation;

import com.madmax.Models.Vehicle;

import java.util.Random;

public class FuelLossEvent extends Event {

    private final Random rand = new Random();

    public FuelLossEvent(double probability) {
        super(probability);
    }

    @Override
    public boolean apply(Vehicle vehicle) {

        int fuel = vehicle.getFuel();
        int loss = Math.max(1, (int)(fuel * (0.05 + (rand.nextDouble() * 0.10))));
        vehicle.consumeFuel(loss);

        return false;

    }

}
