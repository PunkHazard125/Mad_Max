package com.madmax.Models;

public class Step {

    private Outpost outpost;
    private int fuelCost;

    public Step(Outpost outpost, int fuelCost) {
        this.outpost = outpost;
        this.fuelCost = fuelCost;
    }

    public Outpost getOutpost() {
        return outpost;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    @Override
    public String toString() {
        return outpost.getName() + " | Supply: " + outpost.getFuelSupply() + " | Cost: " + fuelCost;
    }

}
