package com.madmax.Models;

import java.util.ArrayList;

public class Outpost {

    private int id;
    private String name;
    private int fuelSupply;
    private int riskLevel;
    private ArrayList<Item> items;

    public Outpost() {

        items = new ArrayList<>();

    }

    public Outpost(int num, String str, int f, int r) {

        id = num;
        name = str;
        fuelSupply = f;
        riskLevel = r;

        items = new ArrayList<>();

    }

    public Outpost(int num, String str, int f, int r, ArrayList<Item> list) {

        id = num;
        name = str;
        fuelSupply = f;
        riskLevel = r;

        items = new ArrayList<>(list);

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFuelSupply() {
        return fuelSupply;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFuelSupply(int fuelSupply) {
        this.fuelSupply = fuelSupply;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
