package com.example.cse_110_team14;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.*;

public class PlanActivity extends AppCompatActivity {

    public RecyclerView planRecyclerView;
    public PlanListAdapter adapter;
    public TextView planSize;

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


//        List<String> fullDirections = new ArrayList<>();
//        for(int i = 0; i < truePath.size(); i++) {
//            GraphPath<String, IdentifiedWeightedEdge> path = truePath.get(i);
//            String directions = directions(g,path, truePathNames.get(i), truePathNames.get(i+1));
////            Log.d("PlanActivity", directions);
//            fullDirections.add(directions);
//        }

        planSize = findViewById(R.id.plan_size_display);
        planSize.setText("(" + plannedAnimals.size() + ")");

        List<Pair<String, Integer>> planList = new ArrayList<>();
        String exhibitName;
        Integer totalPathDistance = 0;
        Pair<String, Integer> planPair;
        for (int i  = 0; i < truePath.size(); ++i) {
            GraphPath<String, IdentifiedWeightedEdge> path = truePath.get(i);
            if (i != truePath.size()) {
                exhibitName = truePathNames.get(i+1);
            }
            else {
                exhibitName = "entrance_exit_gate";
            }
            totalPathDistance += (int) path.getWeight();
            planPair = new Pair<>(exhibitName, totalPathDistance);
            planList.add(planPair);
        }

        adapter = new PlanListAdapter(planList);
        adapter.setHasStableIds(true);

        planRecyclerView = findViewById(R.id.plan_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        planRecyclerView.setLayoutManager(layoutManager);
        planRecyclerView.setAdapter(adapter);

        //Add dividers between recyclerView items
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(planRecyclerView.getContext(),layoutManager.getOrientation());
        planRecyclerView.addItemDecoration(dividerItemDecoration);

        // The list fullDirections will contain the entire path from entrance to exit to each of the
        // planned animals and then back to the entrance
        // The i-th element of fullDirections will be the directions to the i-th planned animal
        // from the i-1th animal.
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

    public String directions(Graph<String, IdentifiedWeightedEdge> g,
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
        String currentSt = null;
        String curr = animalIdToName.get(start);
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            sb.append(" " + i + ". ");
            if(eInfo.get(e.getId()).street.equals(currentSt)) {
                sb.append("Continue on " + eInfo.get(e.getId()).street + " ");
            }
            else {
                sb.append("Proceed on " + eInfo.get(e.getId()).street +" ");
            }
            currentSt = eInfo.get(e.getId()).street;
            String target = vInfo.get(g.getEdgeTarget(e).toString()).name;
            String source = vInfo.get(g.getEdgeSource(e).toString()).name;

            sb.append((int) g.getEdgeWeight(e) + " ft towards" );

            if(curr.equals(target)) {
                sb.append(" " + source + ".\n");
                curr = source;
            }
            else {
                sb.append(" " + target + ".\n");
                curr = target;
            }

            i++;
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
