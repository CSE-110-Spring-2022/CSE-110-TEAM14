package com.example.cse_110_team14;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class U5Test {
    @Test
    public void testCheckedAnimals() {
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.searchRecyclerView;

            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(0);
            vh.itemView.findViewById(R.id.search_item_checkbox).performClick();

            List<CheckedName> l = activity.itemsDao.getAll();
            assertFalse(l.isEmpty());

            vh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            l = activity.itemsDao.getAll();
            assert(l.isEmpty());
        });
    }

    @Test
    public void testActivityRetention(){
        ActivityScenario<PlanActivity> scenario = ActivityScenario.launch(PlanActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.DESTROYED);

        ActivityScenario<MainActivity> newscen = ActivityScenario.launch(MainActivity.class);
        newscen.moveToState(Lifecycle.State.CREATED);

        newscen.onActivity(activity -> {
            String name = ActivityData.getActivity(activity.getApplicationContext(), "activity.json");
            assertEquals(name, "PlanActivity");
        });
    }



}
