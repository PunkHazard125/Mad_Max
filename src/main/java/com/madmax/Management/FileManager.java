package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.madmax.Models.Route;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
        Type outpostListType = new TypeToken<ArrayList<Outpost>>() {}.getType();
        return loadFromFile(OUTPOST_FILE, outpostListType);
    }

    public static ArrayList<Route> loadRoutes() {
        Type routeListType = new TypeToken<ArrayList<Route>>() {}.getType();
        return loadFromFile(ROUTE_FILE, routeListType);
    }

}
