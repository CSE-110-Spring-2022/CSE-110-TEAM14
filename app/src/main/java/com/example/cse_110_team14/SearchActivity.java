package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public EditText searchBar;
    public SearchListAdapter adapter;
    public List<ZooData.VertexInfo> animalList;

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
        animalList = new ArrayList<>(animalMap.values());
        this.adapter = new SearchListAdapter(animalList);
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.search_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        searchBar = findViewById(R.id.search_text);

        adapter.setSearchItems(animalList);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void filter(Editable editable) {
        List<ZooData.VertexInfo> newSearchItems = new ArrayList<>();
        if (editable.toString().isEmpty() || (editable.toString().trim().equals(""))) {
            recyclerView.setAdapter(new SearchListAdapter(animalList));
            adapter.notifyDataSetChanged();
        }
        else {
            String newText = editable.toString().toLowerCase();
            for(int index = 0; index < animalList.size(); ++index){
                if(animalList.get(index).name.toLowerCase().contains(newText)){
                    newSearchItems.add(animalList.get(index));
                }
            }
            recyclerView.setAdapter(new SearchListAdapter(newSearchItems));
        }
    }
}