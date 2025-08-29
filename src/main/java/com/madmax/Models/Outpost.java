package com.madmax.Models;

import com.madmax.Simulation.Event;

import java.util.ArrayList;
import java.util.Iterator;

public class Outpost {

    private int id;
    private String name;
    private int fuelSupply;
    private ArrayList<Event> events;
    private ArrayList<Item> items;

    public Outpost() {

        events = new ArrayList<>();
        items = new ArrayList<>();

    }

    public Outpost(int id, String name, int fuelSupply, ArrayList<Event> events, ArrayList<Item> items) {

        this.id = id;
        this.name = name;
        this.fuelSupply = fuelSupply;

        this.events = new ArrayList<>();
        this.items = new ArrayList<>();

        this.events.addAll(events);

        for (Item item : items) {

            this.items.add(new Item(item));

        }

    }

    public Outpost(Outpost outpost) {

        this.id = outpost.getId();
        this.name = outpost.getName();
        this.fuelSupply = outpost.getFuelSupply();

        this.events = new ArrayList<>();
        this.items = new ArrayList<>();

        this.events.addAll(outpost.getEvents());

        for (Item item : outpost.getItems()) {

            this.items.add(new Item(item));

        }

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

    public ArrayList<Event> getEvents() {
        return events;
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

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void removeItem(Item item) {

        Iterator<Item> iter = items.iterator();

        while (iter.hasNext()) {

            Item curr = iter.next();

            if (curr.getId() == item.getId()) {

                iter.remove();
                break;

            }

        }

    }


}
