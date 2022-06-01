package com.example.cse_110_team14;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Map;

public interface DirectionsInterface {
    /**
     * Returns a string of directions based on the given graph, path, start, and goal
     * @param g The graph to use.
     * @param path The path to use.
     * @param start The start of the path
     * @param goal The goal of the path
     * @param animalIdToName The map of animal ids to animal names.
     * @param vInfo The vertexes to use
     * @param eInfo The edges to use
     * @return A string of directions based on the given graph, path, start, and goal
     */
    public String directions(Graph<String, IdentifiedWeightedEdge> g,
                                            GraphPath<String, IdentifiedWeightedEdge> path,
                                            String start, String goal, Map<String, String> animalIdToName,
                                            Map<String, ZooData.VertexInfo> vInfo,
                                            Map<String, ZooData.EdgeInfo> eInfo);


}
