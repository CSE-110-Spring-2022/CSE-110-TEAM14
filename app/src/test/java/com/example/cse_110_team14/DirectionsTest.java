package com.example.cse_110_team14;

import static org.junit.Assert.*;


import android.content.Intent;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
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
                    = ZooData.loadZooGraphJSON(activity, "zoo_graph.json",
                    "zoo_node_info.json",
                    "zoo_edge_info.json");

            assertEquals(activity.fullDirections.toString(), "[]");

        });
    }
}

