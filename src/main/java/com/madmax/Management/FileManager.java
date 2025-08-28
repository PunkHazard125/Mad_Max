package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.madmax.Models.OutpostDTO;
import com.madmax.Models.Route;
import com.madmax.Simulation.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    private static final String OUTPOST_FILE = "/json/outposts.json";
    private static final String ROUTE_FILE = "/json/routes.json";
    private static final Gson gson = new Gson();

    private static <T> ArrayList<T> loadFromFile(String resourcePath, Type type) {
        InputStream is = FileManager.class.getResourceAsStream(resourcePath);
        if (is == null) {
            System.err.println("Resource not found: " + resourcePath);
            return new ArrayList<>();
        }

        try (Reader reader = new InputStreamReader(is)) {
            ArrayList<T> data = gson.fromJson(reader, type);
            return (data != null) ? data : new ArrayList<>();
        }
        catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<Outpost> loadOutposts() {
        Type outpostListType = new TypeToken<ArrayList<OutpostDTO>>() {}.getType();
        ArrayList<OutpostDTO> dtoList = loadFromFile(OUTPOST_FILE, outpostListType);

        ArrayList<Outpost> outposts = new ArrayList<>();

        for (OutpostDTO curr : dtoList) {

            ArrayList<Event> events = new ArrayList<>();
            if (curr.getEvents() != null) {

                for (EventDTO ev : curr.getEvents()) {

                    Event event = null;

                    switch (ev.getType().toLowerCase()) {
                        case "none" -> event = new NoEvent(ev.getProbability());
                        case "fuel_loss" -> event = new FuelLossEvent(ev.getProbability());
                        case "fuel_bonus" -> event = new FuelBonusEvent(ev.getProbability());
                        case "loot" -> event = new LootEvent(ev.getProbability());
                        case "ambush" -> event = new AmbushEvent(ev.getProbability());
                        default -> throw new IllegalStateException("Unexpected value: " + ev.getType().toLowerCase());
                    }

                    if (event != null) {
                        events.add(event);
                    }
                }
            }

            Outpost outpost = new Outpost(curr.getId(), curr.getName(), curr.getFuelSupply(), events, curr.getItems());
            outposts.add(outpost);

        }

        return outposts;

    }

    public static ArrayList<Route> loadRoutes() {
        Type routeListType = new TypeToken<ArrayList<Route>>() {}.getType();
        return loadFromFile(ROUTE_FILE, routeListType);
    }

}
