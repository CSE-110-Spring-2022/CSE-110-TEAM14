package com.example.cse_110_team14;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.widget.TextView;

import android.os.Handler;

@RunWith(AndroidJUnit4.class)
public class SelectedExhibitTest {
    @Rule
    public ActivityScenarioRule<SearchActivity> scenarioRule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void displaySelectedExhibits() {
        ActivityScenario<SearchActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            RecyclerView searchRecyclerView = activity.searchRecyclerView;
            RecyclerView.ViewHolder searchVh = searchRecyclerView.findViewHolderForAdapterPosition(0);
            searchVh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            assertEquals("Crocodiles", activity.selectedList.get(0));
            assertEquals(1, activity.selectedListAdapter.getItemCount());
        });
    }
}
