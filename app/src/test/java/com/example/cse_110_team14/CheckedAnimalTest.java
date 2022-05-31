package com.example.cse_110_team14;

import static org.junit.Assert.assertFalse;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CheckedAnimalTest {


    // Makes sure all animals are unchecked at start
    @Test
    public void initiallyNoCheckedAnimals() {
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            //activity
            List<CheckedName> l = activity.itemsDao.getAll();

            for(var v : l)
                System.out.println(v.name);

            if(l.size() == 0)
                System.out.println("WTDFLKJADSLKFJF");
            assert(activity.checkedAnimals().isEmpty());
        });
    }

    @Test
    public void addCheckedAnimal() {
        //ActivityScenario<SearchActivity> scenario = scenarioRule.getScenario();
        ActivityScenario<SearchActivity> scenario = ActivityScenario.launch(SearchActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.searchRecyclerView;


            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(0);
            vh.itemView.findViewById(R.id.search_item_checkbox).performClick();
            // There should be one checked animal!!
            assertFalse(activity.checkedAnimals().isEmpty());

            List<CheckedName> l = activity.itemsDao.getAll();

            for(var v : l)
                System.out.println(v.name);
        });
    }



}
