package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisitAnimalActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public Button nextButton;
    public Button previousButton;
    public TextView animalName;
    public DirectionListAdapter adapter;
    public int currIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_animal);

        ArrayList<String> fullDirections =
                getIntent().getStringArrayListExtra("full_directions");
        ArrayList<String> animalsInOrder =
                getIntent().getStringArrayListExtra("animal_order");
        ArrayList<Integer> distancesInOrder =
                getIntent().getIntegerArrayListExtra("distances");


        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        animalName = findViewById(R.id.animalName);

        List<List<String>> stepByStepDirections = new ArrayList<>();
        for (String s : fullDirections) {
            stepByStepDirections.add(Arrays.asList(s.split("\n")));
        }

        adapter = new DirectionListAdapter(stepByStepDirections.get(0));
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.directionsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setDirections(stepByStepDirections.get(0));
        adapter.notifyDataSetChanged();

        previousButton.setText("");
        previousButton.setEnabled(false);
        previousButton.setAlpha(.8f);
        String temp1 = "Next " + animalsInOrder.get(currIndex + 1) +
                " (" + distancesInOrder.get(currIndex + 1) + " ft)";
        nextButton.setText(temp1);

        animalName.setText(animalsInOrder.get(currIndex));

        previousButton.setOnClickListener(v -> {
            currIndex--;
            adapter.setDirections(stepByStepDirections.get(currIndex));
            adapter.notifyDataSetChanged();
            animalName.setText(animalsInOrder.get(currIndex));
            nextButton.setEnabled(true);
            nextButton.setAlpha(1f);
            String temp = "Next " + animalsInOrder.get(currIndex + 1) + " (" +
                    distancesInOrder.get(currIndex + 1) + " ft)";
            nextButton.setText(temp);

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

            adapter.setDirections(stepByStepDirections.get(currIndex));
            adapter.notifyDataSetChanged();
            animalName.setText(animalsInOrder.get(currIndex));

            previousButton.setEnabled(true);
            previousButton.setAlpha(1f);
            String temp = "Previous " + animalsInOrder.get(currIndex - 1) +
                    " (" + distancesInOrder.get(currIndex - 1) + " ft)";
            previousButton.setText(temp);
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
}