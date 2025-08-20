package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.madmax.Models.Route;
import com.madmax.Models.Step;
import com.madmax.Models.Vehicle;

import java.util.*;

public class JourneyUtils {

    public static void dijkstra(int src, int dest, ArrayList<ArrayList<Route>> adjList, ArrayList<Outpost> outposts,
                                ArrayList<Outpost> path, ArrayList<Step> steps) {

        int n = adjList.size();

        int[] dist = new int[n];
        int[] parent = new int[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        dist[src] = 0;
        pq.offer(new int[]{src, 0});

        while (!pq.isEmpty()) {

            int[] curr = pq.poll();
            int u = curr[0], d = curr[1];

            if (d > dist[u]) {
                continue;
            }

            for (Route v : adjList.get(u)) {

                if (dist[v.get_dest()] > dist[u] + v.get_fuel_cost()) {

                    dist[v.get_dest()] = dist[u] + v.get_fuel_cost();
                    pq.offer(new int[]{v.get_dest(), dist[v.get_dest()]});
                    parent[v.get_dest()] = u;

                }
            }
        }

        path.clear();
        steps.clear();

        if (dist[dest] == Integer.MAX_VALUE) {

            return;

        }

        for (int curr = dest; curr != -1; curr = parent[curr]) {

            path.add(outposts.get(curr));

        }

        Collections.reverse(path);

        for (Outpost o : path) {

            int cost = dist[o.getId()];
            steps.add(new Step(o, cost));

        }

    }

    public static int detour(int currentLoc, ArrayList<ArrayList<Route>> adjList, ArrayList<Outpost> outposts,
                             Vehicle warRig) {

        int fuel = warRig.getFuel();
        int newLoc = -1;
        int fuelCost = Integer.MAX_VALUE;

        for (Route r : adjList.get(currentLoc)) {

            if (r.get_fuel_cost() <= fuelCost && r.get_fuel_cost() <= fuel) {

                newLoc = r.get_dest();
                fuelCost = r.get_fuel_cost();

            }

        }

        if (newLoc != -1) {

            warRig.consumeFuel(fuelCost);
            warRig.setLocationId(newLoc);
            warRig.refuel(outposts.get(newLoc).getFuelSupply());

        }

        return newLoc;

    }

}
