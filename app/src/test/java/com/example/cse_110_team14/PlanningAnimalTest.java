package com.example.cse_110_team14;

import static org.junit.Assert.*;


import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

@RunWith(AndroidJUnit4.class)
public class PlanningAnimalTest {


    @Rule
    public ActivityScenarioRule<PlanActivity> scenarioRule =
            new ActivityScenarioRule<>(PlanActivity.class);

    //public PlanActivity pa = new PlanActivity();

    @Test
    public void planFirstAnimal() {


        ActivityScenario<PlanActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {

            Graph<String, IdentifiedWeightedEdge> g
                    = ZooData.loadZooGraphJSON(activity, "sample_zoo_graph.json");
            ArrayList<String>  plannedAnimals = new ArrayList<String>();
            plannedAnimals.add("gorillas");
            plannedAnimals.add("gators");


            List<String> path = new ArrayList<String>();
            path.add("entrance_exit_gate");
            path.add("gators");
            path.add("gorillas");
            path.add("entrance_exit_gate");
            assertEquals(path, activity.shortestPath(plannedAnimals, g,   "entrance_exit_gate", "entrance_exit_gate").second);

        });
    }

    @Test
    public void testPathLength() {
        ActivityScenario<PlanActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            Graph<String, IdentifiedWeightedEdge> g
                    = ZooData.loadZooGraphJSON(activity, "sample_zoo_graph.json");


            assertEquals(210, activity.pathLength(g, activity.shortestPathHelper("entrance_exit_gate", "gorillas", g)));
        });
    }

    @Test
    public void testDirections() {
        ActivityScenario<PlanActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            Graph<String, IdentifiedWeightedEdge> g
                    = ZooData.loadZooGraphJSON(activity, "sample_zoo_graph.json");

            String direct = " 1. Proceed on Entrance Way 10 ft towards Entrance Plaza.\n 2. Proceed on Africa Rocks Street 200 ft towards Gorillas.\n";
        assertEquals(direct, activity.directions(g,
                activity.shortestPathHelper("entrance_exit_gate", "gorillas", g), "entrance_exit_gate", "gorillas"));

        });
    }
}
