package com.madmax.Simulation;

import com.madmax.Models.Vehicle;

public abstract class Event {
    private final double probability;

    public Event(double probability) {
        this.probability = probability;
    }

    public double getProbability() { return probability; }

    public abstract boolean apply(Vehicle vehicle);

}
