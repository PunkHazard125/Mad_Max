package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.madmax.Models.Route;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileManager {

    private static final String OUTPOST_FILE = "outposts.json";
    private static final String ROUTE_FILE = "routes.json";
    private static final Gson gson = new Gson();

    private static <T> void saveToFile(String fileName, ArrayList<T> data) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static <T> ArrayList<T> loadFromFile(String fileName, Type type) {
        try (Reader reader = new FileReader(fileName)) {
            ArrayList<T> data = gson.fromJson(reader, type);
            return (data != null) ? data : new ArrayList<>();
        }
        catch (NullPointerException ex) {
            return new ArrayList<>();
        }
        catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public static void saveOutposts(ArrayList<Outpost> outposts) {
        saveToFile(OUTPOST_FILE, outposts);
    }

    public static ArrayList<Outpost> loadOutposts() {
        Type outpostListType = new TypeToken<ArrayList<Outpost>>() {}.getType();
        return loadFromFile(OUTPOST_FILE, outpostListType);
    }

    public static void saveRoutes(ArrayList<Route> routes) {
        saveToFile(ROUTE_FILE, routes);
    }

    public static ArrayList<Route> loadRoutes() {
        Type routeListType = new TypeToken<ArrayList<Route>>() {}.getType();
        return loadFromFile(ROUTE_FILE, routeListType);
    }

}
