package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.json.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Uncomment this line of code out if u want to go back to search activity since I have not
        // implemented a way to go back to the start yet
        ActivityData.setActivity(this, "activity.json", "SearchActivity");
        String activityName = ActivityData.getActivity(this, "activity.json");
        Intent intent = new Intent(this, SearchActivity.class);;

        Log.d("MainActivity", "activityName: " + activityName);

        if(activityName.equals("SearchActivity")) {
            intent = new Intent(this, SearchActivity.class);
        }

        else if(activityName.equals("PlanActivity")) {
            Log.d("MainActivity", "activityName: " + activityName);
            ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
            List<CheckedName> checkedNames = itemsDao.getAll();
            ArrayList<String> test = new ArrayList<>();
            for(CheckedName name : checkedNames) {
                test.add(name.name);
            }

            Log.d("MainActivity", "exhibitList: " + test);

            intent = new Intent(this, PlanActivity.class);
            intent.putExtra("checked_animals", test);

        }

        else if(activityName.equals("VisitAnimalActivity")) {
            Log.d("MainActivity", "activityName: " + activityName);
            ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
            List<CheckedName> checkedNames = itemsDao.getAll();
            ArrayList<String> test = new ArrayList<>();
            for(CheckedName name : checkedNames) {
                test.add(name.name);
            }
            Map<String, String> animalNameToId = new HashMap<>();
            Map<String, String> animalIdToName = new HashMap<>();
            Map<String, ZooData.VertexInfo> animalMap =
                    ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
            // List of ALL animals
            List<ZooData.VertexInfo> animalList = new ArrayList<>(animalMap.values());

            ArrayList<String> plannedAnimalsIds = new ArrayList<>();
            for (ZooData.VertexInfo animal : animalList) {
                animalIdToName.put(animal.id, animal.name);
                animalNameToId.put(animal.name, animal.id);
            }

            for (String animal : test) {
                plannedAnimalsIds.add(animalNameToId.get(animal));
            }
            Graph<String, IdentifiedWeightedEdge> g
                    = ZooData.loadZooGraphJSON(this, "zoo_graph.json");

            ArrayList<String> animalsInOrder = new ArrayList<>();
            ArrayList<String> exhibitIDsInOrder = new ArrayList<>();
            ArrayList<String> fullDirections = new ArrayList<>();
            ArrayList<Integer> distancesInOrder = new ArrayList<>();
            ArrayList<String> briefDirections = new ArrayList<>();
            // Calculates the shortest path to visit all vertices
            Pair<List<GraphPath<String, IdentifiedWeightedEdge>>,List<String>> truePathPair =
                    PlanActivity.shortestPath(plannedAnimalsIds, g,
                            "entrance_exit_gate", "entrance_exit_gate");

            // List of paths from one planned animal to another
            List<GraphPath<String, IdentifiedWeightedEdge>> truePath = truePathPair.first;
            List<String> truePathNames = truePathPair.second;
            String exhibitName;
            Integer totalPathDistance = 0;
            Pair<String, Integer> planPair;

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

            for (int i  = 0; i < truePath.size(); ++i) {
                GraphPath<String, IdentifiedWeightedEdge> path = truePath.get(i);
                exhibitName = animalIdToName.get(truePathNames.get(i+1));
                totalPathDistance += (int) path.getWeight();
                planPair = new Pair<>(exhibitName, totalPathDistance);
                animalsInOrder.add(exhibitName);
                exhibitIDsInOrder.add(truePathNames.get(i+1));
                distancesInOrder.add((int)(path.getWeight()));
            }



            intent = new Intent(this, VisitAnimalActivity.class);
            intent.putExtra("animal_order", animalsInOrder);
            intent.putExtra("exhibit_id_order", exhibitIDsInOrder);
            intent.putExtra("full_directions", fullDirections);
            intent.putExtra("distances", distancesInOrder);
            intent.putExtra("brief_directions", briefDirections);
            intent.putExtra("directions", ActivityData.getDirections
                    (this,"directions.json"));
            intent.putExtra("index", ActivityData.getDirectionsIndex
                    (this, "index.json"));
        }


        startActivity(intent);
    }
}