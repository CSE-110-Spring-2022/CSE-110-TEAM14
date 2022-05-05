package com.example.cse_110_team14;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class UserStory1Test {
    @Test
    public void testSearchBehavior() {
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            EditText searchBar = activity.searchBar;
            RecyclerView recyclerView = activity.recyclerView;

            searchBar.requestFocus();
            searchBar.setText("Gorilla");
            searchBar.clearFocus();

            assertEquals("Gorilla", (searchBar.getText()).toString());
        });
    }
}
