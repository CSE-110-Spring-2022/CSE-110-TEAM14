package com.example.cse_110_team14;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class PlanActivity extends AppCompatActivity {

    public Map<String, String> animalIdToName = new HashMap<>();
    public Map<String, String> animalNameToId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        // List of planned animal names
        ArrayList<String> plannedAnimals =
                getIntent().getStringArrayListExtra("checked_animals");


        Map<String, ZooData.VertexInfo> animalMap =
                ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        // List of ALL animals
        List<ZooData.VertexInfo> animalList = new ArrayList<>(animalMap.values());

        for (ZooData.VertexInfo animal : animalList) {
            animalIdToName.put(animal.id, animal.name);
            animalNameToId.put(animal.name, animal.id);
        }
        ArrayList<String> plannedAnimalsIds = new ArrayList<>();

        for (String animal : plannedAnimals) {
            plannedAnimalsIds.add(animalNameToId.get(animal));
        }
        Graph<String, IdentifiedWeightedEdge> g
                = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");
        shortestPath(plannedAnimalsIds, g);
    }

    // This method finds the order of vertexes that the user should visit
    public void shortestPath(ArrayList<String> plannedAnimals,
                             Graph<String, IdentifiedWeightedEdge> g) {
        String start = "entrance_exit_gate";
        String goal = null;
        for (int i = 0; i < plannedAnimals.size(); i++) {
            goal = plannedAnimals.get(i);
            shortestPathHelper(start, goal, g);
        }
    }

    private void shortestPathHelper(String start, String goal,
                                    Graph<String, IdentifiedWeightedEdge> g) {
        GraphPath<String, IdentifiedWeightedEdge> path =
                DijkstraShortestPath.findPathBetween(g, start, goal);
        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo =
                ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo =
                ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        Log.d("PlanActivity", "The shortest path from '" + animalIdToName.get(start) + " to '" +
                animalIdToName.get(goal) + " is:\n");
        int i = 1;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            Log.d("PlanActivity", "  " + i + ". Walk " + g.getEdgeWeight(e) + " meters along " + eInfo.get(e.getId()).street + " from " + vInfo.get(g.getEdgeSource(e).toString()).name + " to " + vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
        }
    }
}
