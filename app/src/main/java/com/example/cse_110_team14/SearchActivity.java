package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        SearchListAdapter adapter = new SearchListAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.search_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        // Example of animal Object:
        /*
        {
            id='elephant_odyssey',
            kind=EXHIBIT,
            name='Elephant Odyssey',
            tags=[elephant, mammal, africa]
         }
         */

        /* So what we want to do in this activity is if the string they are searching for equals
           name OR one of the tags, then we want to display the animal.
         */

        // Map from animal id to animal object
        Map<String, ZooData.VertexInfo> animalMap =
                ZooData.loadVertexInfoJSON(this, "sample_node_info.json");


        // List of animals
        List<ZooData.VertexInfo> animalList = new ArrayList<>(animalMap.values());

        adapter.setSearchItems(animalList);

    }
}