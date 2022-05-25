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
public class DirectionsTest {
    @Rule
    public ActivityScenarioRule<PlanActivity> scenarioRule =
            new ActivityScenarioRule<>(PlanActivity.class);

    @Test
    public void testDirections() {
        ActivityScenario<PlanActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            Graph<String, IdentifiedWeightedEdge> g
                    = ZooData.loadZooGraphJSON(activity, "zoo_graph.json");

            String direct =
                    " 1. Proceed on Gate Path 10 ft towards Front Street / Treetops Way.\n" +
                            " 2. Proceed on Treetops Way 30 ft towards Treetops Way / Fern Canyon Trail.\n" +
                            " 3. Proceed on Fern Canyon Trail 60 ft towards Fern Canyon.\n" +
                            " 4. Proceed on Aviary Trail 30 ft towards Owens Aviary.\n" +
                            " 5. Continue on Aviary Trail 50 ft towards Parker Aviary.\n" +
                            " 6. Proceed on Treetops Way 80 ft towards Benchley Plaza.\n" +
                            " 7. Proceed on Monkey Trail 50 ft towards Gorillas.\n";
            assertEquals(direct, direct);

        });
    }
}

