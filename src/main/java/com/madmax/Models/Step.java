package com.madmax.Models;

public class Step {

    private Outpost outpost;
    private int fuelCost;
    private double successRate;

    public Step(Outpost outpost, int fuelCost, double prob) {

        this.outpost = outpost;
        this.fuelCost = fuelCost;
        this.successRate = prob;

    }

    public Outpost getOutpost() {
        return outpost;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    @Override
    public String toString() {

        if (getFuelCost() == 0) {

            return outpost.getName()
                    + " | Current Location";

        }

        return outpost.getName()
                + " | Cost: " + fuelCost
                + " | Rate: " + String.format("%.2f", successRate * 100) + "% ";
    }

}
