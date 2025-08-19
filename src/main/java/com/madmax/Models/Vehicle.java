package com.madmax.Models;

import java.util.ArrayList;

public class Vehicle {

    private int fuel;
    private ArrayList<Item> cargo;
    private final int fuelCapacity;
    private final int cargoCapacity;
    private int locationId;

    public Vehicle(int f, int c) {

        fuelCapacity = f;
        cargoCapacity = c;
        fuel = 0;
        locationId = 1;
        cargo = new ArrayList<>();

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

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public void refuel(int amount) {
        fuel = Math.min(fuel + amount, fuelCapacity);
    }

    public void ConsumeFuel(int amount) {
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

}
