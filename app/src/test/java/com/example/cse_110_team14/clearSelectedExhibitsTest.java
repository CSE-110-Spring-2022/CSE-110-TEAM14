package com.example.cse_110_team14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.Button;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class clearSelectedExhibitsTest {

    @Rule
    public ActivityScenarioRule<SearchActivity> scenarioRule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void clearSelectedExhibit() {
        ActivityScenario<SearchActivity> scenario = scenarioRule.getScenario();

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            RecyclerView searchRecyclerView = activity.searchRecyclerView;
            RecyclerView.ViewHolder searchVh = searchRecyclerView.findViewHolderForAdapterPosition(0);
            searchVh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            Button clearBtn = activity.clearSelectedListBtn;
            clearBtn.performClick();
            assertTrue(activity.getCheckedExhibits().isEmpty());
            assertTrue(activity.presenter.getSelectedList().isEmpty());
        });
    }

}
