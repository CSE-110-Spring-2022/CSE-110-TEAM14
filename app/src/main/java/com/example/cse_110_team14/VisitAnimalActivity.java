package com.example.cse_110_team14;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// This is where you get directions
public class VisitAnimalActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Button nextButton;
    public Button previousButton;
    public Button skipButton;
    public Button mockButton;
    public Button reEnableGPSButton;
    public TextView animalName;
    public DirectionListAdapter adapter;
    public int currIndex;
    public boolean detailed;
    public DirectionsInterface directionsStrategy;
    public Graph<String, IdentifiedWeightedEdge> g;
    public Map<String,
            ZooData.VertexInfo> vInfo;
    public Map<String, ZooData.EdgeInfo> eInfo;

    public List<String> exhibitIDsInOrder;
    public List<String> animalsInOrder;
    public boolean offRouteCalled = false;

    PermissionChecker permissionChecker;
    LocationManager locationManager;
    LocationListener locationListener;
    public List<ZooData.VertexInfo> futureExhibits;

    public static final String EXTRA_LISTEN_TO_GPS = "listen_to_gps";

    @VisibleForTesting
    public VisitExhibitPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_animal);


        g = ZooData.loadZooGraphJSON(this, "zoo_graph.json",
                "zoo_node_info.json",
                "zoo_edge_info.json");
        vInfo = ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this, "zoo_edge_info.json");
        ActivityData.setActivity(this, "activity.json", "VisitAnimalActivity");

        // Getting the directions, animal name, and distances from the previous activity

        List<Pair<Integer, String>> distancePairs = new ArrayList<>();

        for(ZooData.VertexInfo info : vInfo.values()) {
            distancePairs.add(new Pair<>(distance(info.id,"gorilla"), info.name));
        }
        Collections.sort(distancePairs, Comparator.comparing(p -> p.first));
        for(Pair<Integer, String> pair : distancePairs) {
            Log.d("allDistances", pair.second + " " + pair.first);
        }
        animalsInOrder =
                getIntent().getStringArrayListExtra("animal_order");
        exhibitIDsInOrder =
                getIntent().getStringArrayListExtra("exhibit_id_order");


        List<ZooData.VertexInfo> vertexList = new ArrayList<>(vInfo.values());

        String directions = getIntent().getStringExtra("directions");
        int index = getIntent().getIntExtra("index", 0);

        currIndex = index;
        detailed = directions.equals("detailed");

        var listenToGps = getIntent().getBooleanExtra(EXTRA_LISTEN_TO_GPS, true);

        Log.d("VisitAnimalActivity", "animalsInOrder: " + animalsInOrder);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        skipButton = findViewById(R.id.skipButton);
        animalName = findViewById(R.id.animalName);
        mockButton = findViewById(R.id.mockButton);
        reEnableGPSButton = findViewById(R.id.reEnableGPSButton);

        // Splits the direction by line to show the directions in a recycler view


        //
        adapter = new DirectionListAdapter(new ArrayList<>());
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.directionsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if (detailed) {
            directionsStrategy = new DetailedDirections();
        } else {
            directionsStrategy = new BriefDirections();
        }


        if (index == 0) {
            // previous button initially disabled
            previousButton.setText("");
            previousButton.setEnabled(false);
            previousButton.setAlpha(.8f);
        } else {
            String temp23 = "Previous " + animalsInOrder.get(currIndex - 1) + " (" +
                    distance(currentLocation(), exhibitIDsInOrder.get(currIndex - 1)) + ")"
                    + " ft)";
            previousButton.setText(temp23);
        }
        if (index < animalsInOrder.size() - 1) {
            String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                    distance(currentLocation(), exhibitIDsInOrder.get(currIndex + 1)) + " ft)";
            nextButton.setText(temp);
            skipButton.setText("Skip\n" + animalsInOrder.get(currIndex));
            skipButton.setEnabled(true);
            skipButton.setAlpha(1f);
        } else {
            nextButton.setText("");
            nextButton.setText("Finish");
            skipButton.setText("");
            skipButton.setEnabled(false);
            skipButton.setAlpha(0.8f);
        }



        animalName.setText(animalsInOrder.get(currIndex));

        Map<String, ZooData.VertexInfo> animalMap =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");

        // List of ALL visiting exhibits
        List<ZooData.VertexInfo> visitList = new ArrayList<>();
        for (String exhibitID : exhibitIDsInOrder) {
            Log.d("visitList", "" + exhibitID);
            visitList.add(animalMap.get(exhibitID));
        }

        VisitExhibitModel model = new ViewModelProvider(this).get(VisitExhibitModel.class);
        presenter = new VisitExhibitPresenter(this, model);
        presenter.updateLatsAndLngs(vertexList);
        futureExhibits = new ArrayList<>();
        futureExhibits.addAll(visitList);
        presenter.updateCurrExhibitDisplayed(animalMap.get(exhibitIDsInOrder.get(0)), futureExhibits);

        previousButton.setOnClickListener(v -> {
            offRouteCalled = false;
            // decrement the index when the previous button is clicked and changes the nextButton
            currIndex--;
            ActivityData.setDirectionsIndex(this, "index.json", currIndex);
            Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));
            ZooData.VertexInfo currExhibitDisplayed = animalMap.get(exhibitIDsInOrder.get(currIndex));
            futureExhibits.clear();
            if ((currIndex + 1) != visitList.size()) {
                for (int i = currIndex + 1; i < visitList.size(); ++i) {
                    futureExhibits.add(visitList.get(i));
                }
            }
            presenter.updateCurrExhibitDisplayed(currExhibitDisplayed, futureExhibits);

            adapter.setDirections(getDirections());
            adapter.notifyDataSetChanged();
            skipButton.setText("Skip\n" + animalsInOrder.get(currIndex));
            skipButton.setEnabled(true);
            skipButton.setAlpha(1f);
            animalName.setText(animalsInOrder.get(currIndex));
            nextButton.setEnabled(true);
            nextButton.setAlpha(1f);
            String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                    distance(currentLocation(), exhibitIDsInOrder.get(currIndex + 1)) + " ft)";
            nextButton.setText(temp);

            // if the index is >0, then the previous button is enabled, otherwise it is disabled
            if (currIndex > 0) {
                temp = "Previous " + animalsInOrder.get(currIndex - 1) + " (" +
                        distance(currentLocation(), exhibitIDsInOrder.get(currIndex - 1))
                        + " ft)";
                previousButton.setText(temp);
            } else {
                previousButton.setText("");
                previousButton.setEnabled(false);
                previousButton.setAlpha(.8f);
            }
        });

        nextButton.setOnClickListener(v -> {
            offRouteCalled = false;
            currIndex++;
            if (currIndex == animalsInOrder.size()) {
                currIndex --;
                finishVisit();
            } else {


                ActivityData.setDirectionsIndex(this, "index.json", currIndex);
                Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));
                ZooData.VertexInfo currExhibitDisplayed =
                        animalMap.get(exhibitIDsInOrder.get(currIndex));
                futureExhibits.clear();
                if ((currIndex + 1) != visitList.size()) {
                    for (int i = currIndex + 1; i < visitList.size(); ++i) {
                        futureExhibits.add(visitList.get(i));
                    }
                }
                presenter.updateCurrExhibitDisplayed(currExhibitDisplayed, futureExhibits);

                // Sets the previous button
                adapter.setDirections(getDirections());
                adapter.notifyDataSetChanged();
                skipButton.setText("Skip\n" + animalsInOrder.get(currIndex));
                skipButton.setEnabled(true);
                skipButton.setAlpha(1f);
                animalName.setText(animalsInOrder.get(currIndex));

                previousButton.setEnabled(true);
                previousButton.setAlpha(1f);
                String temp = "Previous " + animalsInOrder.get(currIndex - 1) +
                        " (" + distance(currentLocation(), exhibitIDsInOrder.get(currIndex - 1)) + " ft)";
                previousButton.setText(temp);

                // If the index is < the size of the list - 1 , then the next button is enabled,
                // otherwise it is disabled
                if (currIndex < animalsInOrder.size() - 1) {
                    temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                            distance(currentLocation(), exhibitIDsInOrder.get(currIndex + 1)) +
                            " ft)";
                    nextButton.setText(temp);
                } else {
                    nextButton.setText("Finish");
                    skipButton.setText("");
                    skipButton.setEnabled(false);
                    skipButton.setAlpha(.8f);
                }
            }

        });

        // Permission Checking
        permissionChecker = new PermissionChecker(this);
        permissionChecker.ensurePermissions();
        // If GPS is disabled (such as in a test), don't listen for updates from real GPS.
        if (listenToGps) {
            while (!permissionChecker.hasPermissions()) {
            }
            setupLocationListener(presenter::updateLastKnownCoords);
        }

        skipButton.setOnClickListener(v -> {
            offRouteCalled = false;

            ArrayList<String> visitedAnimals = new ArrayList<>();
            ArrayList<String> visitedIds = new ArrayList<>();
            for (int i = 0; i < currIndex; ++i) {
                visitedAnimals.add(animalsInOrder.get(i));
                visitedIds.add(exhibitIDsInOrder.get(i));
            }
            ArrayList<String> unvisitedAnimals = new ArrayList<>();
            ArrayList<String> unvisitedIds = new ArrayList<>();
            for (int i = currIndex + 1; i < animalsInOrder.size() - 1; ++i) {
                unvisitedAnimals.add(animalsInOrder.get(i));
                unvisitedIds.add(exhibitIDsInOrder.get(i));
            }
            Pair<List<GraphPath<String, IdentifiedWeightedEdge>>, List<String>> truePathPair =
                    PlanActivity.shortestPath(unvisitedIds, g,
                            currentLocation(), "entrance_exit_gate", vInfo);

            List<String> newOrder = truePathPair.second;
            Log.d("NewOrder: ", newOrder.toString());
            for (int i = 1; i < newOrder.size(); ++i) {
                visitedIds.add(newOrder.get(i));
                visitedAnimals.add(vInfo.get(newOrder.get(i)).name);
            }
            Log.d("NewOrder: ", visitedIds.toString());

            Log.d("NewOrder: ", visitedAnimals.toString());

            exhibitIDsInOrder = visitedIds;
            animalsInOrder = visitedAnimals;
            ActivityData.setAnimals(this, "animals.json", animalsInOrder);
            ActivityData.setIds(this, "ids.json", exhibitIDsInOrder);

            ActivityData.setDirectionsIndex(this, "index.json", currIndex);
            Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));
            ZooData.VertexInfo currExhibitDisplayed =
                    animalMap.get(exhibitIDsInOrder.get(currIndex));
            futureExhibits.clear();
            if ((currIndex + 1) != visitList.size()) {
                for (int i = currIndex + 1; i < visitList.size(); ++i) {
                    futureExhibits.add(visitList.get(i));
                }
            }
            presenter.updateCurrExhibitDisplayed(currExhibitDisplayed, futureExhibits);

            // Sets the previous button
            adapter.setDirections(getDirections());
            adapter.notifyDataSetChanged();
            skipButton.setText("Skip\n" + animalsInOrder.get(currIndex));
            skipButton.setEnabled(true);
            skipButton.setAlpha(1f);
            animalName.setText(animalsInOrder.get(currIndex));


            if(currIndex == 0) {
                previousButton.setText("");
                previousButton.setEnabled(false);
                previousButton.setAlpha(.8f);
            }
            else {
                previousButton.setEnabled(true);
                previousButton.setAlpha(1f);
                String temp = "Previous " + animalsInOrder.get(currIndex - 1) +
                        " (" + distance(currentLocation(), exhibitIDsInOrder.get(currIndex - 1))
                        + " ft)";
                previousButton.setText(temp);
            }
            // If the index is < the size of the list - 1 , then the next button is enabled,
            // otherwise it is disabled
            if (currIndex < animalsInOrder.size() - 1) {
                String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                        distance(currentLocation(),exhibitIDsInOrder.get(currIndex + 1)) +
                        " ft)";
                nextButton.setText(temp);
            } else {
                nextButton.setText("Finish");
                skipButton.setText("");
                skipButton.setEnabled(false);
                skipButton.setAlpha(.8f);
            }
        });
        adapter.setDirections(getDirections());
        adapter.notifyDataSetChanged();

        mockButton.setOnClickListener(v -> {
            disableGPS();
            var inputType = EditorInfo.TYPE_CLASS_NUMBER
                    | EditorInfo.TYPE_NUMBER_FLAG_SIGNED
                    | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;

            final EditText latInput = new EditText(this);
            latInput.setInputType(inputType);
            latInput.setHint("Latitude");

            final EditText lngInput = new EditText(this);
            lngInput.setInputType(inputType);
            lngInput.setHint("Longitude");

            final LinearLayout layout = new LinearLayout(this);
            layout.setDividerPadding(8);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(latInput);
            layout.addView(lngInput);

            var builder = new AlertDialog.Builder(this)
                    .setTitle("Inject a Mock Location")
                    .setView(layout)
                    .setPositiveButton("Submit", (dialog, which) -> {
                        var lat = Double.parseDouble(latInput.getText().toString());
                        var lng = Double.parseDouble(lngInput.getText().toString());
                        presenter.updateLastKnownCoords(Pair.create(lat, lng));
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                    });
            builder.show();
            Log.d("ClosestVertex is:", getClosestVertex());
        });

        reEnableGPSButton.setOnClickListener(v -> {
            reEnableGPS();
        });
    }

    private void finishVisit() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityData.setActivity(this, "activity.json", "SearchActivity");
        ActivityData.setDirectionsIndex(this, "index.json", 0);
        ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
        itemsDao.deleteAll();

        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void setupLocationListener(Consumer<Pair<Double, Double>> handleNewCoords) {
        // Connect location listener to the model.
        var provider = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                var coords = Pair.create(
                        location.getLatitude(),
                        location.getLongitude()
                );
                handleNewCoords.accept(coords);
            }
        };
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
    }

    // This method is called when you press the settings button
    public void clickNew(View view) {
        final TextView replan = new EditText(this);
        replan.setText("Brief or detailed directions?");

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(replan);

        var builder = new AlertDialog.Builder(this)
                .setTitle("Settings")
                .setView(layout)
                .setPositiveButton("Brief directions", (dialog, which) -> {
                    detailed = false;
                    directionsStrategy = new BriefDirections();
                    ActivityData.setDirections(this, "directions.json",
                            detailed ? "detailed" : "brief");

                    adapter.setDirections(getDirections());
                    adapter.notifyDataSetChanged();
                })
                .setNegativeButton("Detailed directions", (dialog, which) -> {
                    detailed = true;
                    directionsStrategy = new DetailedDirections();
                    ActivityData.setDirections(this, "directions.json",
                            detailed ? "detailed" : "brief");

                    adapter.setDirections(getDirections());
                    adapter.notifyDataSetChanged();
                });
        builder.show();

    }

    public List<String> getDirections() {
        Log.d("getDirections", exhibitIDsInOrder.get(currIndex));
        Map<String, String> animalIdToString = new HashMap<>();
        for (ZooData.VertexInfo animal : vInfo.values()) {
            animalIdToString.put(animal.id, animal.name);
        }


        Log.d("getDirections", animalIdToString.toString());
        String temp = directionsStrategy.directions(g,
                PlanActivity.shortestPathHelper(
                        currentLocation(),
                        exhibitIDsInOrder.get(currIndex),
                        g,
                        vInfo
                ), currentLocation(),
                exhibitIDsInOrder.get(currIndex),
                animalIdToString, vInfo, eInfo);
        List<String> ans = new ArrayList<>();
        for (String s : temp.split("\n")) {
            ans.add(s);
        }
        if (ans.size() == 0 || ans.get(0).equals("")) {
            Log.d("getDirections", "empty");
            if (ans.size() == 1) {
                ans = new ArrayList<>();
            }
            ans.add("You are here!");
        } else {
            Log.d("getDirections", ans.toString());
            Log.d("getDirections", "" + ans.size());
            Log.d("getDirections", "" + ans.get(0));
        }
        return ans;
    }

    public String currentLocation() {
        return getClosestVertex();
    }

    public void offRoutePrompt() {
        if(offRouteCalled){
            return;
        }
        final TextView replan = new EditText(this);
        replan.setText("You are off route. Would you like to replan?");

        final LinearLayout layout = new LinearLayout(this);
        layout.setDividerPadding(8);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(replan);

        var builder = new AlertDialog.Builder(this)
                .setTitle("Off Route")
                .setView(layout)
                .setPositiveButton("Yes", (dialog, which) -> {
                    replanBackend();
                    Log.d("offRoute","replanned!");
                })
                .setNegativeButton("No", (dialog, which) -> {
                    Log.d("offRoute","cancelled!");
                });
        builder.show();
        Log.d("offRoutePrompt", "called " + getClosestVertex());
        offRouteCalled = true;
    }
    public void replanBackend(){
        offRouteCalled = false;
        Map<String, ZooData.VertexInfo> animalMap =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        ArrayList<String> visitedAnimals = new ArrayList<>();
        ArrayList<String> visitedIds = new ArrayList<>();
        for (int i = 0; i < currIndex; ++i) {
            visitedAnimals.add(animalsInOrder.get(i));
            visitedIds.add(exhibitIDsInOrder.get(i));
        }
        ArrayList<String> unvisitedAnimals = new ArrayList<>();
        ArrayList<String> unvisitedIds = new ArrayList<>();
        for (int i = currIndex; i < animalsInOrder.size() - 1; ++i) {
            unvisitedAnimals.add(animalsInOrder.get(i));
            unvisitedIds.add(exhibitIDsInOrder.get(i));
        }
        Pair<List<GraphPath<String, IdentifiedWeightedEdge>>, List<String>> truePathPair =
                PlanActivity.shortestPath(unvisitedIds, g,
                        currentLocation(), "entrance_exit_gate", vInfo);

        List<String> newOrder = truePathPair.second;
        Log.d("NewOrder: ", newOrder.toString());
        for (int i = 1; i < newOrder.size(); ++i) {
            visitedIds.add(newOrder.get(i));
            visitedAnimals.add(vInfo.get(newOrder.get(i)).name);
        }
        Log.d("NewOrder2: ", visitedIds.toString());

        Log.d("NewOrder2: ", visitedAnimals.toString());

        exhibitIDsInOrder = visitedIds;
        animalsInOrder = visitedAnimals;
        ActivityData.setAnimals(this, "animals.json", animalsInOrder);
        ActivityData.setIds(this, "ids.json", exhibitIDsInOrder);
        ActivityData.setDirectionsIndex(this, "index.json", currIndex);
        Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));

        ZooData.VertexInfo currExhibitDisplayed =
                animalMap.get(exhibitIDsInOrder.get(currIndex));
        futureExhibits.clear();
        List<ZooData.VertexInfo> visitList = new ArrayList<>();
        for (String exhibitID : exhibitIDsInOrder) {
            Log.d("visitList", "" + exhibitID);
            visitList.add(animalMap.get(exhibitID));
        }
        if ((currIndex + 1) != visitList.size()) {
            for (int i = currIndex + 1; i < visitList.size(); ++i) {
                futureExhibits.add(visitList.get(i));
            }
        }

        presenter.updateCurrExhibitDisplayed(currExhibitDisplayed, futureExhibits);

        // Sets the previous button
        adapter.setDirections(getDirections());
        adapter.notifyDataSetChanged();
        skipButton.setText("Skip\n" + animalsInOrder.get(currIndex));
        skipButton.setEnabled(true);
        skipButton.setAlpha(1f);
        animalName.setText(animalsInOrder.get(currIndex));


        if(currIndex == 0) {
            previousButton.setText("");
            previousButton.setEnabled(false);
            previousButton.setAlpha(.8f);
        }
        else {
            previousButton.setEnabled(true);
            previousButton.setAlpha(1f);
            String temp = "Previous " + animalsInOrder.get(currIndex - 1) +
                    " (" + distance(currentLocation(), exhibitIDsInOrder.get(currIndex - 1))
                    + " ft)";
            previousButton.setText(temp);
        }
        // If the index is < the size of the list - 1 , then the next button is enabled,
        // otherwise it is disabled
        if (currIndex < animalsInOrder.size() - 1) {
            String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                    distance(currentLocation(),exhibitIDsInOrder.get(currIndex + 1)) +
                    " ft)";
            nextButton.setText(temp);
        } else {
            nextButton.setText("Finish");
            skipButton.setText("");
            skipButton.setEnabled(false);
            skipButton.setAlpha(.8f);
        }

    }

    public String getClosestVertex() {
        if(presenter == null) return "gorilla";
        return presenter.getClosestVertex();
    }

    public int distance(String v1, String v2) {
        GraphPath<String, IdentifiedWeightedEdge> a =
                PlanActivity.shortestPathHelper(v1, v2, g, vInfo);
        return (int) a.getWeight();
    }


    public void disableGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        locationManager = null;
    }

    public void reEnableGPS() {
        setupLocationListener(presenter::updateLastKnownCoords);
    }
}