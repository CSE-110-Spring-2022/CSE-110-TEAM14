package com.example.cse_110_team14;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.Map;

public interface DirectionsInterface {

    public String directions(Graph<String, IdentifiedWeightedEdge> g,
                                            GraphPath<String, IdentifiedWeightedEdge> path,
                                            String start, String goal, Map<String, String> animalIdToName,
                                            Map<String, ZooData.VertexInfo> vInfo,
                                            Map<String, ZooData.EdgeInfo> eInfo);


}
