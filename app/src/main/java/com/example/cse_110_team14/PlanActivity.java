package com.example.cse_110_team14;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

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

        Pair<List<GraphPath<String, IdentifiedWeightedEdge>>,List<String>> truePathPair =
                shortestPath(plannedAnimalsIds, g,
                        "entrance_exit_gate", "entrance_exit_gate");

        List<GraphPath<String, IdentifiedWeightedEdge>> truePath = truePathPair.first;
        List<String> truePathNames = truePathPair.second;

        for(int i = 0; i < truePath.size(); i++) {
            GraphPath<String, IdentifiedWeightedEdge> path = truePath.get(i);

            Log.d("PlanActivity",
                    edgeListToString(g,path, truePathNames.get(i), truePathNames.get(i+1)));
        }
    }

    // This method finds the order of vertexes that the user should visit
    public Pair<List<GraphPath<String, IdentifiedWeightedEdge>>,List<String>>
                            shortestPath(ArrayList<String> plannedAnimals,
                                         Graph<String, IdentifiedWeightedEdge> g,
                                         String start, String goal) {

        List<String> visited = new ArrayList<>();
        List<GraphPath<String, IdentifiedWeightedEdge>> truePath  = new ArrayList<>();
        visited.add(start);
        while(plannedAnimals.size() > 0) {
            List<Integer> possiblePathLengths = new ArrayList<>();
            List<String> possiblePaths = new ArrayList<>();
            List<GraphPath<String, IdentifiedWeightedEdge>> possiblePathsList = new ArrayList<>();
            for (int i = 0; i < plannedAnimals.size(); i++) {
                String goal2 = plannedAnimals.get(i);
                GraphPath<String, IdentifiedWeightedEdge> path = shortestPathHelper(start, goal2, g);
                int length = pathLength(g, path);
                possiblePathLengths.add(length);
                possiblePaths.add(goal2);
                possiblePathsList.add(path);
            }
            int min = Integer.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < possiblePathLengths.size(); i++) {
                if (possiblePathLengths.get(i) < min) {
                    min = possiblePathLengths.get(i);
                    index = i;
                }
            }
            String goal2 = possiblePaths.get(index);
            plannedAnimals.remove(goal2);
            start = goal2;
            truePath.add(possiblePathsList.get(index));
            visited.add(goal2);
        }

        truePath.add(shortestPathHelper(start, goal, g));
        visited.add(goal);

        return new Pair<>(truePath, visited);
    }

    private GraphPath<String, IdentifiedWeightedEdge> shortestPathHelper(String start, String goal,
                                    Graph<String, IdentifiedWeightedEdge> g) {
        GraphPath<String, IdentifiedWeightedEdge> path =
                DijkstraShortestPath.findPathBetween(g, start, goal);

        return path;
    }

    public String edgeListToString(Graph<String, IdentifiedWeightedEdge> g,
                                   GraphPath<String, IdentifiedWeightedEdge> path,
                                   String start, String goal) {

        StringBuilder sb = new StringBuilder();
//        sb.append("The shortest path from " +
//                animalIdToName.get(start) + " to " +
//                animalIdToName.get(goal) + " is:\n");
        Map<String, ZooData.VertexInfo> vInfo =
                ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo =
                ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        int i = 1;
        String current = null;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            sb.append(" " + i + ". ");
            if(eInfo.get(e.getId()).street.equals(current)) {
                sb.append("Continue on " + eInfo.get(e.getId()).street);
            }
            else {
                sb.append("Proceed on  " + eInfo.get(e.getId()).street);
            }
            current = eInfo.get(e.getId()).street;
//            sb.append(" " + i + ".Walk " + g.getEdgeWeight(e) + " meters along " +
//                    eInfo.get(e.getId()).street + " from " +
//                    street1 + " to " +
//                    street2 + "\n");
//            i++;
//            start = street2;
        }

        return sb.toString();
    }
    public int pathLength(Graph<String, IdentifiedWeightedEdge> g,
                          GraphPath<String, IdentifiedWeightedEdge> path) {
        int length = 0;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            length += g.getEdgeWeight(e);
        }

        return length;
    }


}
