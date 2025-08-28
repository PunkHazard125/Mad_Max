package com.madmax.Models;

import com.madmax.Simulation.EventDTO;
import java.util.ArrayList;

public class OutpostDTO {
    private int id;
    private String name;
    private int fuelSupply;
    private ArrayList<Item> items;
    private ArrayList<EventDTO> events;

    public OutpostDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFuelSupply() {
        return fuelSupply;
    }

    public void setFuelSupply(int fuelSupply) {
        this.fuelSupply = fuelSupply;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<EventDTO> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventDTO> events) {
        this.events = events;
    }

}
