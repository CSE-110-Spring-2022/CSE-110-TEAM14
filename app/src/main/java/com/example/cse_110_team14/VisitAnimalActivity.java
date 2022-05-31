package com.example.cse_110_team14;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.Arrays;
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

        animalsInOrder =
                getIntent().getStringArrayListExtra("animal_order");
        exhibitIDsInOrder =
                getIntent().getStringArrayListExtra("exhibit_id_order");
        ArrayList<Integer> distancesInOrder =
                getIntent().getIntegerArrayListExtra("distances");



        String directions = getIntent().getStringExtra("directions");
        int index = getIntent().getIntExtra("index",0);

        currIndex = index;
        detailed = directions.equals("detailed");

        var listenToGps = getIntent().getBooleanExtra(EXTRA_LISTEN_TO_GPS, true);

        Log.d("VisitAnimalActivity", "animalsInOrder: " + animalsInOrder);
        Log.d("VisitAnimalActivity", "distancesInOrder: " + distancesInOrder);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        skipButton = findViewById(R.id.skipButton);
        animalName = findViewById(R.id.animalName);

        // Splits the direction by line to show the directions in a recycler view


        //
        adapter = new DirectionListAdapter(new ArrayList<>());
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.directionsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if(detailed) {
            directionsStrategy = new DetailedDirections();
        }
        else {
            directionsStrategy = new BriefDirections();
        }


        if(index == 0) {
            // previous button initially disabled
            previousButton.setText("");
            previousButton.setEnabled(false);
            previousButton.setAlpha(.8f);
        }
        else{
            String temp23 = "Previous " + animalsInOrder.get(currIndex - 1) + " (" +
                    distancesInOrder.get(currIndex - 1) + " ft)";
            previousButton.setText(temp23);
        }
        if(index < animalsInOrder.size() - 1) {
            String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                    distancesInOrder.get(currIndex + 1) + " ft)";
            nextButton.setText(temp);
        }
        else {
//            nextButton.setText("");
//            nextButton.setEnabled(false);
//            nextButton.setAlpha(.8f);
            nextButton.setText("Finish");
        }

        skipButton.setText("Skip\n" + animalsInOrder.get(currIndex));

        animalName.setText(animalsInOrder.get(currIndex));

        Map<String, ZooData.VertexInfo> animalMap =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");

        // List of ALL visiting exhibits
        List<ZooData.VertexInfo> visitList = new ArrayList<>();
        for (String exhibitID : exhibitIDsInOrder) {
            Log.d("errorcheck", "" + exhibitID);
            visitList.add(animalMap.get(exhibitID));
        }

        previousButton.setOnClickListener(v -> {
            // decrement the index when the previous button is clicked and changes the nextButton
            currIndex--;
            ActivityData.setDirectionsIndex(this, "index.json", currIndex);
            Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));
            ZooData.VertexInfo currExhibit = animalMap.get(exhibitIDsInOrder.get(currIndex));
            presenter.updateCurrExhibit(currExhibit);

            adapter.setDirections(getDirections());
            adapter.notifyDataSetChanged();
            skipButton.setText("Skip\n"+animalsInOrder.get(currIndex));
            animalName.setText(animalsInOrder.get(currIndex));
            nextButton.setEnabled(true);
            nextButton.setAlpha(1f);
            String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                    distancesInOrder.get(currIndex + 1) + " ft)";
            nextButton.setText(temp);

            // if the index is >0, then the previous button is enabled, otherwise it is disabled
            if(currIndex > 0) {
                temp = "Previous " + animalsInOrder.get(currIndex - 1) + " (" +
                        distancesInOrder.get(currIndex - 1) + " ft)";
                previousButton.setText(temp);
            }
            else {
                previousButton.setText("");
                previousButton.setEnabled(false);
                previousButton.setAlpha(.8f);
            }
        });

        nextButton.setOnClickListener(v -> {
            currIndex++;
            if (currIndex == animalsInOrder.size()) {
                finishVisit();
            }
            ActivityData.setDirectionsIndex(this, "index.json", currIndex);
            Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));
            ZooData.VertexInfo currExhibit = animalMap.get(exhibitIDsInOrder.get(currIndex));
            presenter.updateCurrExhibit(currExhibit);

            // Sets the previous button
            adapter.setDirections(getDirections());
            adapter.notifyDataSetChanged();
            skipButton.setText("Skip\n"+animalsInOrder.get(currIndex));
            animalName.setText(animalsInOrder.get(currIndex));

            previousButton.setEnabled(true);
            previousButton.setAlpha(1f);
            String temp = "Previous " + animalsInOrder.get(currIndex - 1) +
                    " (" + distancesInOrder.get(currIndex - 1) + " ft)";
            previousButton.setText(temp);

            // If the index is < the size of the list - 1 , then the next button is enabled,
            // otherwise it is disabled
            if (currIndex < animalsInOrder.size() - 1) {
                temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                        distancesInOrder.get(currIndex + 1) + " ft)";
               nextButton.setText(temp);
            }
            else {
                nextButton.setText("Finish");
                //skipButton.setText("");
                //skipButton.setEnabled(false);
                //skipButton.setAlpha(.8f);
            }
        });

        VisitExhibitModel model = new ViewModelProvider(this).get(VisitExhibitModel.class);
        presenter = new VisitExhibitPresenter(this, model);
        presenter.updateLatsAndLngs(visitList);
        // If GPS is disabled (such as in a test), don't listen for updates from real GPS.
        if (listenToGps) setupLocationListener();


        skipButton.setOnClickListener(v -> {
            ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
            itemsDao.delete(animalName.getText().toString());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        adapter.setDirections(getDirections());
        adapter.notifyDataSetChanged();
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
    private void setupLocationListener() {
        // Permission Checking
        if (new PermissionChecker(this).ensurePermissions()) return;

        // Connect location listener to the model.
        var provider = LocationManager.GPS_PROVIDER;
        var locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        var locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                var coords = Pair.create(
                        location.getLatitude(),
                        location.getLongitude()
                );
                presenter.updateLastKnownCoords(coords);
            }
        };
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);
    }

    // This method is called when you press the settings button
    public void clickNew(View view) {
        detailed = !detailed;
        if(detailed) {
            directionsStrategy = new DetailedDirections();

        }
        else {
            directionsStrategy = new BriefDirections();
        }

        ActivityData.setDirections(this, "directions.json",
                detailed ? "detailed":"brief");

        adapter.setDirections(getDirections());
        adapter.notifyDataSetChanged();

    }

    public List<String> getDirections() {
        Log.d("getDirections", exhibitIDsInOrder.get(currIndex));
        Map<String,String> animalIdToString = new HashMap<>();
        for (ZooData.VertexInfo animal : vInfo.values()) {
            animalIdToString.put(animal.id, animal.name);
        }


        Log.d("getDirections", animalIdToString.toString());
        String temp =  directionsStrategy.directions(g,
                    PlanActivity.shortestPathHelper(
                            currentLocation(),
                            exhibitIDsInOrder.get(currIndex),
                            g,
                            vInfo
                    ), currentLocation(),
                    exhibitIDsInOrder.get(currIndex),
                    animalIdToString, vInfo,eInfo);
        List<String> ans = new ArrayList<>();
        for(String s : temp.split("\n")) {
            ans.add(s);
        }
        if(ans.size() ==0 || ans.get(0).equals("")) {
            Log.d("getDirections", "empty");
            if(ans.size() == 1) {
                ans = new ArrayList<>();
            }
            ans.add("You are here!");
        }
        else {
            Log.d("getDirections", ans.toString());
            Log.d("getDirections", "" + ans.size());
            Log.d("getDirections", "" + ans.get(0));
        }
        return ans;
    }

    public void popupActivity(){
        Intent planIntent = new Intent(this, PopupActivity.class);
        startActivity(planIntent);
    }

    public String currentLocation(){
        return "entrance_exit_gate";
    }
}