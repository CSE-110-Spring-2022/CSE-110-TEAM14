package com.example.cse_110_team14;

import static org.junit.Assert.assertFalse;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CheckedAnimalTest {


    // Makes sure all animals are unchecked at start
    @Test
    public void initiallyNoCheckedAnimals() {
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            assert(activity.checkedAnimals().isEmpty());
        });
    }

    @Test
    public void addCheckedAnimal() {
        //ActivityScenario<SearchActivity> scenario = scenarioRule.getScenario();
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.recyclerView;


            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(0);
            vh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            // There should be one checked animal!!
            assertFalse(activity.checkedAnimals().isEmpty());


        });
    }



}
