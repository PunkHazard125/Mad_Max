package com.madmax.Models;

import java.util.ArrayList;

public class Vehicle {

    private int fuel;
    private ArrayList<Item> cargo;
    private final int fuelCapacity;
    private final int cargoCapacity;
    private int locationId;
    private int credits;
    private int fuelConsumption;
    private int eventsEncountered;
    private boolean ambushed;

    public Vehicle(int f, int c) {

        fuelCapacity = f;
        cargoCapacity = c;
        fuel = 0;
        locationId = 1;
        credits = 0;
        cargo = new ArrayList<>();
        fuelConsumption = 0;
        eventsEncountered = 0;
        ambushed = false;

    }

    public Vehicle(Vehicle o) {

        fuel = o.getFuel();
        cargo = new ArrayList<>();
        fuelCapacity = o.getFuelCapacity();
        cargoCapacity = o.getCargoCapacity();
        credits = o.getCredits();
        fuelConsumption = o.fuelConsumption;
        eventsEncountered = o.eventsEncountered;
        ambushed = o.isAmbushed();

    }

    public int getFuel() {
        return fuel;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getCredits() {
        return credits;
    }

    public ArrayList<Item> getCargo() {
        return cargo;
    }

    public int getCurrentWeight() {

        int weight = 0;

        for (Item item : cargo) {

            weight += item.getWeight();

        }

        return weight;
    }

    public int getProfit() {

        int total = 0;

        for (Item item : cargo) {

            total += item.getValue();

        }

        return total;

    }

    public int getFuelConsumption() {
        return fuelConsumption;
    }

    public int getEventCount() {
        return eventsEncountered;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void refuel(int amount) {
        fuel = Math.min(fuel + amount, fuelCapacity);
    }

    public void consumeFuel(int amount) {
        fuelConsumption += Math.min(amount, fuel);
        fuel = Math.max(fuel - amount, 0);
    }

    public boolean addItem(Item item) {

        if (getCurrentWeight() + item.getWeight() <= getCargoCapacity()) {

            cargo.add(item);
            return  true;

        }

        return false;

    }

    public void removeItem(Item item) {

        cargo.remove(item);

    }

    public void addCredits(int amount) {
        credits += amount;
    }

    public void addEventCount() {
        eventsEncountered++;
    }

    public boolean isAmbushed() {
        return ambushed;
    }

    public void setAmbushed(boolean ambushed) {
        this.ambushed = ambushed;
    }

}
