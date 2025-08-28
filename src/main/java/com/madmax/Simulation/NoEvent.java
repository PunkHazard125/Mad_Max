package com.madmax.Simulation;

import com.madmax.Models.Vehicle;

public class NoEvent extends Event {

    public NoEvent(double probability) {
        super(probability);
    }

    @Override
    public boolean apply(Vehicle vehicle) {

        return false;

    }
}
