package com.madmax.Models;

public class Item {

    private int id;
    private String name;
    private int weight;
    private int value;

    public Item() {}

    public Item(int num, String str, int w, int v) {

        id = num;
        name = str;
        weight = w;
        value = v;

    }

    public Item(Item o) {

        id = o.getId();
        name = o.getName();
        weight = o.getWeight();
        value = o.getValue();

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
