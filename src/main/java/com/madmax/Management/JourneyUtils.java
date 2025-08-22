package com.madmax.Management;

import com.madmax.Models.Outpost;
import com.madmax.Models.Route;
import com.madmax.Models.Step;
import com.madmax.Models.Vehicle;

import java.util.*;

public class JourneyUtils {

    private static final Random rand = new Random();
    private static final double alpha = 0.7;

    public static void dijkstra(int src, int dest, ArrayList<ArrayList<Route>> adjList, ArrayList<Outpost> outposts,
                                ArrayList<Outpost> path, ArrayList<Step> steps, Vehicle warRig) {

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

        for (int i = 0; i < path.size(); i++) {

            int cost = dist[path.get(i).getId()];
            double prob;

            if (i == 0) {

                prob = 1.00;

            }

            else
            {

                prob = monteCarloSim(i, adjList, path, warRig);

            }

            steps.add(new Step(path.get(i), cost, prob));

        }

    }

    public static int detour(int currentLoc, ArrayList<ArrayList<Route>> adjList, ArrayList<Outpost> outposts,
                             Vehicle warRig) {

        int fuel = warRig.getFuel();
        int newLoc = -1;
        int fuelCost = Integer.MAX_VALUE;

        for (Route r : adjList.get(currentLoc)) {

            if (r.get_fuel_cost() <= fuelCost && r.get_fuel_cost() <= fuel && outposts.get(r.get_dest()).getRiskLevel() <= 7) {

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

    public static void updateRisk(ArrayList<Step> steps) {

        if (steps.size() <= 1) { return; }

        for (Step step : steps) {

            Outpost curr = step.getOutpost();

            int oldRisk = curr.getRiskLevel();
            double temp = (alpha * oldRisk) + (1 - alpha) * (rand.nextInt(10) + 1);
            int newRisk = (int) Math.round(temp);

            newRisk = Math.max(1, Math.min(10, newRisk));

            curr.setRiskLevel(newRisk);

        }

    }

    public static double monteCarloSim(int dest, ArrayList<ArrayList<Route>> adjList,
                                                  ArrayList<Outpost> path, Vehicle warRig) {

        ArrayList<Outpost> copiedPath = new ArrayList<>();

        for (Outpost o : path) {

            copiedPath.add(new Outpost(o));

        }

        Vehicle v = new Vehicle(warRig);
        v.setLocationId(path.getFirst().getId());

        double probability;
        int success = 0;
        int trials = 1000;

        for (int t = 0; t < trials; t++) {

            Vehicle trialRig = new Vehicle(v);


            boolean reached = true;

            for(int i = 0; i < dest; i++) {

                int cost = 0;

                for (Route r : adjList.get(copiedPath.get(i).getId())) {

                    if (r.get_dest() == copiedPath.get(i + 1).getId()) {

                        cost = r.get_fuel_cost();
                        break;

                    }

                }

                int oldRisk = copiedPath.get(i + 1).getRiskLevel();
                double temp = (alpha * oldRisk) + (1 - alpha) * (rand.nextInt(10) + 1);
                int trialRisk = Math.max(1, Math.min(10, (int)Math.round(temp)));

                if (trialRig.getFuel() < cost || trialRisk > 7) {

                    reached = false;
                    break;

                }

                trialRig.consumeFuel(cost);
                trialRig.setLocationId(copiedPath.get(i + 1).getId());

            }

            if (reached) {

                success++;

            }

        }

        probability = (double) success / trials;
        return probability;

    }

}
