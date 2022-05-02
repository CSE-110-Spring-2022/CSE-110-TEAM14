package com.example.cse_110_team14;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SearchbarTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    private final static int NUM_ANIMALS = 7;
    @Test
    public void allAnimalsInEmptySearchBar() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);


        scenario.onActivity(activity -> {
//            // Makes sure all animals are originally present
//            List<ZooData.VertexInfo> filteredList =
//                    activity.filter(new SpannableStringBuilder(""));
//            assertEquals(filteredList.size(), NUM_ANIMALS);
//
//            filteredList =
//                    activity.filter(new SpannableStringBuilder(""));
//            //Makes sure all animals are present when there is nothing in the search bar
//            assertEquals(filteredList.size(), NUM_ANIMALS);


        });
    }


    @Test
    public void filterAnimalsById() {
        ActivityScenario<MainActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
//            List<ZooData.VertexInfo> filteredList =
//                    activity.filter(new SpannableStringBuilder("Elephant"));
//            assertEquals(filteredList.size(), 1);
//            assertEquals(filteredList.get(0).name, "Elephant Odyssey");


        });

    }

}