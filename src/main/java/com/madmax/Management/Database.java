package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.madmax.Models.Route;
import com.madmax.Models.Item;

import java.util.ArrayList;

public class Database {

    private ArrayList<Outpost> outposts;
    private ArrayList<Route> routes;

    public Database() {

        outposts = FileManager.loadOutposts();
        routes = FileManager.loadRoutes();

    }

}
