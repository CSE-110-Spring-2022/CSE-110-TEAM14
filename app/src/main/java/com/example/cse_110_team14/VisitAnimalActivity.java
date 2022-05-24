package com.example.cse_110_team14;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

// This is where you get directions
public class VisitAnimalActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Button nextButton;
    public Button previousButton;
    public TextView animalName;
    public DirectionListAdapter adapter;
    public int currIndex = 0;

    public static final String EXTRA_LISTEN_TO_GPS = "listen_to_gps";

    @VisibleForTesting
    public VisitExhibitPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_animal);

        // Getting the directions, animal name, and distances from the previous activity
        ArrayList<String> fullDirections =
                getIntent().getStringArrayListExtra("full_directions");
        ArrayList<String> animalsInOrder =
                getIntent().getStringArrayListExtra("animal_order");
        ArrayList<Integer> distancesInOrder =
                getIntent().getIntegerArrayListExtra("distances");
        // Check intent extras for flags.
        var listenToGps = getIntent().getBooleanExtra(EXTRA_LISTEN_TO_GPS, true);

        Log.d("VisitAnimalActivity", "fullDirections: " + fullDirections);
        Log.d("VisitAnimalActivity", "animalsInOrder: " + animalsInOrder);
        Log.d("VisitAnimalActivity", "distancesInOrder: " + distancesInOrder);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        animalName = findViewById(R.id.animalName);

        // Splits the direction by line to show the directions in a recycler view
        List<List<String>> stepByStepDirections = new ArrayList<>();
        for (String s : fullDirections) {
            stepByStepDirections.add(Arrays.asList(s.split("\n")));
        }

        //
        adapter = new DirectionListAdapter(stepByStepDirections.get(0));
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.directionsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setDirections(stepByStepDirections.get(0));
        adapter.notifyDataSetChanged();


        // previous button initially disabled
        previousButton.setText("");
        previousButton.setEnabled(false);
        previousButton.setAlpha(.8f);
        String temp1 = "Next " + animalsInOrder.get(currIndex + 1) +
                " (" + distancesInOrder.get(currIndex + 1) + " ft)";
        nextButton.setText(temp1);

        animalName.setText(animalsInOrder.get(currIndex));

        previousButton.setOnClickListener(v -> {
            // decrement the index when the previous button is clicked and changes the nextButton
            currIndex--;
            Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));

            adapter.setDirections(stepByStepDirections.get(currIndex));
            adapter.notifyDataSetChanged();
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
            Log.d("VisitAnimalActivity", "currIndex: " + animalsInOrder.get(currIndex));
            // Sets the previous button
            adapter.setDirections(stepByStepDirections.get(currIndex));
            adapter.notifyDataSetChanged();
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
                nextButton.setText("");
                nextButton.setEnabled(false);
                nextButton.setAlpha(.8f);
            }
        });

        VisitExhibitModel model = new ViewModelProvider(this).get(VisitExhibitModel.class);
        presenter = new VisitExhibitPresenter(this, model);
        // If GPS is disabled (such as in a test), don't listen for updates from real GPS.
        if (listenToGps) setupLocationListener();
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


    }
}