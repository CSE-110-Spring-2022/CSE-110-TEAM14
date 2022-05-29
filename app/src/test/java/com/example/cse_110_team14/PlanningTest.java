package com.example.cse_110_team14;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.text.SpannableStringBuilder;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class PlanningTest {
    @Rule
    public ActivityScenarioRule<SearchActivity> scenarioRule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void defaultCheck() {
        ActivityScenario<SearchActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            // check default plan button
            String buttontext = (String)activity.planBtn.getText();
            assertEquals(buttontext, "Plan(0)");
            activity.filter(new SpannableStringBuilder("Elephant"));
            assertEquals(buttontext, "Plan(0)");
        });
    }
    @Test
    public void addRemove() {
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            String buttontext = (String)activity.planBtn.getText();
            // after adding and removing
            RecyclerView recyclerView = activity.searchRecyclerView;
            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(0);
            vh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            buttontext = (String)activity.planBtn.getText();
            assertEquals(buttontext, "Plan(1)");
            vh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            buttontext = (String)activity.planBtn.getText();
            assertEquals(buttontext, "Plan(0)");
        });
    }
}