package com.madmax.Simulation;

import com.madmax.Models.Vehicle;

import java.util.Random;

public class LootEvent extends Event {

    private final Random rand = new Random();

    public LootEvent(double probability) {
        super(probability);
    }

    @Override
    public boolean apply(Vehicle vehicle) {

        int loot = Math.max(1, (int)(vehicle.getFuel() * (0.05 + rand.nextDouble() * 0.10)));
        vehicle.addCredits(loot);

        return false;

    }
}
