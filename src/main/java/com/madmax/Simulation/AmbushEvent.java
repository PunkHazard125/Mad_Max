package com.madmax.Simulation;

import com.madmax.Models.Vehicle;

public class AmbushEvent extends Event {

    public AmbushEvent(double probability) {
        super(probability);
    }

    @Override
    public boolean apply(Vehicle vehicle) {

        return true;

    }
}
