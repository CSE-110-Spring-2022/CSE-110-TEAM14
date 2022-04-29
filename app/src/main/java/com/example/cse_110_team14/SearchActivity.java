package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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


    }
}