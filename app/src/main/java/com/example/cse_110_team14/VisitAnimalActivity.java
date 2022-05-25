package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This is where you get directions
public class VisitAnimalActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Button nextButton;
    public Button previousButton;
    public TextView animalName;
    public DirectionListAdapter adapter;
    public int currIndex = 0;
    public boolean detailed = true;
    public List<List<String>> stepByStepDirections = new ArrayList<>();
    public List<List<String>> stepByStepBriefDirections = new ArrayList<>();
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
        ArrayList<String> briefDirections =
                getIntent().getStringArrayListExtra("brief_directions");
        Log.d("VisitAnimalActivity", "fullDirections: " + fullDirections);
        Log.d("VisitAnimalActivity", "animalsInOrder: " + animalsInOrder);
        Log.d("VisitAnimalActivity", "distancesInOrder: " + distancesInOrder);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        animalName = findViewById(R.id.animalName);

        // Splits the direction by line to show the directions in a recycler view

        for (String s : fullDirections) {
            stepByStepDirections.add(Arrays.asList(s.split("\n")));
        }
        for (String s: briefDirections) {
            stepByStepBriefDirections.add(Arrays.asList(s.split("\n")));
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

            if(detailed) adapter.setDirections(stepByStepDirections.get(currIndex));
            else adapter.setDirections(stepByStepBriefDirections.get(currIndex));
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
            if(detailed) adapter.setDirections(stepByStepDirections.get(currIndex));
            else adapter.setDirections(stepByStepBriefDirections.get(currIndex));
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


    }

    // This method is called when you press the settings button
    public void clickNew(View view) {
        detailed = !detailed;
        if(detailed) {
            adapter.setDirections(stepByStepDirections.get(currIndex));
            adapter.notifyDataSetChanged();

        }
        else {
            adapter.setDirections(stepByStepBriefDirections.get(currIndex));
            adapter.notifyDataSetChanged();
        }
    }
}