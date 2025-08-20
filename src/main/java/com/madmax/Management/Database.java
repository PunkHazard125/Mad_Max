package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.madmax.Models.Route;
import com.madmax.Models.Vehicle;

import java.util.ArrayList;

public class Database {

    private Vehicle war_rig;
    private ArrayList<Outpost> outposts;
    private ArrayList<Route> routes;
    private ArrayList<ArrayList<Route>> adjList;

    public Database() {

        outposts = FileManager.loadOutposts();
        routes = FileManager.loadRoutes();

        outposts.add(0, new Outpost());

        war_rig = new Vehicle(60, 120);

        if (!outposts.isEmpty()) {

            war_rig.refuel(outposts.get(1).getFuelSupply() + 10);
            outposts.getFirst().getItems().clear();

        }

        adjList = new ArrayList<>();

        for (int i = 0; i < outposts.size(); i++) {

            adjList.add(new ArrayList<>());

        }

        for (Route r : routes) {

            int src = r.get_src();
            adjList.get(src).add(r);

        }

    }

    public Vehicle getWarRig() {
        return war_rig;
    }

    public ArrayList<Outpost> getOutposts() {
        return outposts;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public ArrayList<ArrayList<Route>> getAdjList() {
        return adjList;
    }

}
