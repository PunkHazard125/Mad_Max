package com.madmax.Models;

public class Route {

    private int src;
    private int dest;
    private int fuel_cost;

    public Route() {}

    public Route(int s, int d, int f) {

        src = s;
        dest = d;
        fuel_cost = f;

    }

    public int get_src() {
        return src;
    }

    public int get_dest() {
        return dest;
    }

    public int get_fuel_cost() {
        return fuel_cost;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public void setFuel_cost(int fuel_cost) {
        this.fuel_cost = fuel_cost;
    }

}
