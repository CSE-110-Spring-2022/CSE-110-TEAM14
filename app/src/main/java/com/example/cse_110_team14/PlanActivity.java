package com.example.cse_110_team14;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.lang.reflect.Array;
import java.util.*;

// This is where you can see the plan you created. It also calculates the route and distances.
public class PlanActivity extends AppCompatActivity {

    public RecyclerView planRecyclerView;
    public PlanListAdapter adapter;
    public TextView planTitle;
    public Button directionsButton;

    public Map<String, String> animalIdToName = new HashMap<>();
    public Map<String, String> animalNameToId = new HashMap<>();

    // List of directions for a user to follow
    ArrayList<String> fullDirections = new ArrayList<>();
    ArrayList<String> briefDirections = new ArrayList<>();

    public ArrayList<String> plannedAnimals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);


        ActivityData.setActivity(this, "activity.json", "PlanActivity");

        Intent visitAnimalIntent = new Intent(this, VisitAnimalActivity.class);
        // List of planned animal names
        if(getIntent().getStringArrayListExtra("checked_animals") != null)
            plannedAnimals.addAll(getIntent().getStringArrayListExtra("checked_animals"));

        Log.d("PlanActivity", "Planned animals: " + plannedAnimals);
        Map<String, ZooData.VertexInfo> animalMap =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        // List of ALL animals
        List<ZooData.VertexInfo> animalList = new ArrayList<>(animalMap.values());

        // Map of animal names to animal IDs and vice versa for easy lookup
        for (ZooData.VertexInfo animal : animalList) {
            animalIdToName.put(animal.id, animal.name);
            animalNameToId.put(animal.name, animal.id);
        }

        // List of animal IDs we plan to visit
        ArrayList<String> plannedAnimalsIds = new ArrayList<>();

        for (String animal : plannedAnimals) {
            plannedAnimalsIds.add(animalNameToId.get(animal));
        }

        Graph<String, IdentifiedWeightedEdge> g
                = ZooData.loadZooGraphJSON(this, "zoo_graph.json");


        // Calculates the shortest path to visit all vertices
        Pair<List<GraphPath<String, IdentifiedWeightedEdge>>,List<String>> truePathPair =
                shortestPath(plannedAnimalsIds, g,
                        "entrance_exit_gate", "entrance_exit_gate");

        // List of paths from one planned animal to another
        List<GraphPath<String, IdentifiedWeightedEdge>> truePath = truePathPair.first;
        // List of animal names in order of visit
        List<String> truePathNames = truePathPair.second;


//        // List of directions for a user to follow
//        ArrayList<String> fullDirections = new ArrayList<>();
//        ArrayList<String> briefDirections = new ArrayList<>();

        Map<String, ZooData.VertexInfo> vInfo =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo =
                ZooData.loadEdgeInfoJSON(this, "zoo_edge_info.json");


        for(int i = 0; i < truePath.size(); i++) {
            GraphPath<String, IdentifiedWeightedEdge> path = truePath.get(i);
            String directions = Directions.detailedDirections(
                    g,path, truePathNames.get(i), truePathNames.get(i+1)
            , animalIdToName, vInfo, eInfo);
            Log.d("PlanActivity", directions);
            fullDirections.add(directions);
            String briefDirection = Directions.briefDirections(
                    g,path, truePathNames.get(i), truePathNames.get(i+1)
                    , animalIdToName, vInfo, eInfo);
            briefDirections.add(briefDirection);
        }

//        // Displays number of animals to visit
//        planTitle = findViewById(R.id.plan_title);
//        planTitle.setText("Plan(" + plannedAnimals.size() + ")");


        // Calculating total distance to display
        List<Pair<String, Integer>> planList = new ArrayList<>();
        String exhibitName;
        Integer totalPathDistance = 0;
        Pair<String, Integer> planPair;

        // Keeps track of distances and animals in order
        ArrayList<String> animalsInOrder = new ArrayList<>();
        ArrayList<String> exhibitIDsInOrder = new ArrayList<>();
        ArrayList<Integer> distancesInOrder = new ArrayList<>();
        for (int i  = 0; i < truePath.size(); ++i) {
            GraphPath<String, IdentifiedWeightedEdge> path = truePath.get(i);
            exhibitName = animalIdToName.get(truePathNames.get(i+1));
            totalPathDistance += (int) path.getWeight();
            planPair = new Pair<>(exhibitName, totalPathDistance);
            planList.add(planPair);
            animalsInOrder.add(exhibitName);
            exhibitIDsInOrder.add(truePathNames.get(i+1));
            distancesInOrder.add((int)(path.getWeight()));
        }

        // Setting up recycler view
        adapter = new PlanListAdapter(planList);
        adapter.setHasStableIds(true);

        planRecyclerView = findViewById(R.id.plan_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        planRecyclerView.setLayoutManager(layoutManager);
        planRecyclerView.setAdapter(adapter);
        directionsButton = findViewById(R.id.directions_button);

        //Add dividers between recyclerView items
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(planRecyclerView.getContext(),layoutManager.getOrientation());
        planRecyclerView.addItemDecoration(dividerItemDecoration);

        // Set up directions button

        ActivityData.setDirectionsIndex(this,"index.json",0);
        String directionsTemp = ActivityData.getDirections(this, "directions.json");
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass in information to DirectionsActivity
                visitAnimalIntent.putExtra("animal_order", animalsInOrder);
                visitAnimalIntent.putExtra("exhibit_id_order", exhibitIDsInOrder);
                visitAnimalIntent.putExtra("full_directions", fullDirections);
                visitAnimalIntent.putExtra("distances", distancesInOrder);
                visitAnimalIntent.putExtra("brief_directions", briefDirections);
                visitAnimalIntent.putExtra("directions", directionsTemp);
                visitAnimalIntent.putExtra("index", 0);
                startActivity(visitAnimalIntent);
            }
        });

    }

    /**
     * This method finds the order of vertexes that the user should visit.
     * @param plannedAnimals List of animals that the user wants to visit.
     * @param g The graph
     * @param start The starting vertex
     * @param goal The last vertex
     * @return The first list of the pair is the list of directions to get to the i-th animal and
     * the second list is the list of the names of the i-th animal.
     */
    public static Pair<List<GraphPath<String, IdentifiedWeightedEdge>>,List<String>>
                            shortestPath(ArrayList<String> plannedAnimals,
                                         Graph<String, IdentifiedWeightedEdge> g,
                                         String start, String goal) {
        // List of exhibits we visit in order
        List<String> visited = new ArrayList<>();
        // List of directions to get to the i-th exhibit
        List<GraphPath<String, IdentifiedWeightedEdge>> truePath  = new ArrayList<>();
        visited.add(start);
        // Basically run dijkstra's algorithm algorithm for each vertex. Visit the vertex with the
        // shortest distance to the current vertex, and then remove it from the list. Continue until
        // the list is empty. Total runtime is O(n^3log(n))
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

        // Adds the final destination to the list, usually the entrance/exit gate
        truePath.add(shortestPathHelper(start, goal, g));
        visited.add(goal);

        return new Pair<>(truePath, visited);
    }



    /**
     * This method finds the shortest path between two vertices using Dijkstra's algorithm.
     * @param start The starting vertex
     * @param goal The last vertex
     * @param g The graph
     * @return The path between the two vertices.
     */
    public static GraphPath<String, IdentifiedWeightedEdge> shortestPathHelper(String start, String goal,
                                                                               Graph<String, IdentifiedWeightedEdge> g) {
        System.out.println(g.toString());
        System.out.println(start + "," + goal);
        GraphPath<String, IdentifiedWeightedEdge> path =
                DijkstraShortestPath.findPathBetween(g, start, goal);
        return path;
    }



    /**
     * This method finds the weight of the path between two vertices.
     * @param g The graph
     * @param path The path between the two vertices
     * @return The weight of the path.
     */
    public static int pathLength(Graph<String, IdentifiedWeightedEdge> g,
                                 GraphPath<String, IdentifiedWeightedEdge> path) {
        int length = 0;
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            length += g.getEdgeWeight(e);
        }

        return length;
    }


}
