package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


// This is where you can search and select what animals you want to visit.
public class SearchActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public EditText searchBar;
    public SearchListAdapter adapter;
    public List<ZooData.VertexInfo> animalList;
    public ImageButton deleteSearchBar;
    public Button planButton;
    public TextView noSearchResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent planIntent = new Intent(this, PlanActivity.class);

        // Map from animal id to animal object
        Map<String, ZooData.VertexInfo> vertexInfoMap =
                ZooData.loadVertexInfoJSON(this, "sample_node_info.json");

        List<ZooData.VertexInfo> vertexList = new ArrayList<>(vertexInfoMap.values());

        // List of vertexes
        animalList = new ArrayList<>();

        // Filters out non-exhibit vertexes
        for (ZooData.VertexInfo vertex : vertexList) {
            if (vertex.kind.toString().equals("EXHIBIT")) {
                animalList.add(vertex);
            }
        }

        // Initialize recycler view and plan button
        adapter = new SearchListAdapter(animalList);
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.search_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        searchBar = findViewById(R.id.search_text);
        deleteSearchBar = findViewById(R.id.delete_btn);
        planButton = findViewById(R.id.plan_button);
        planButton.setText("Plan(0)");
        planButton.setEnabled(false);
        noSearchResults = findViewById(R.id.no_search_results);
        noSearchResults.setVisibility(View.INVISIBLE);

        adapter.setSearchItems(animalList);

        //Add dividers between recyclerView items
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setSas(new SAStorage(this));

        //called when the search bar is being edited
        planButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Go to next activity and pass the list of animals they want to visit
                List<ZooData.VertexInfo> vi_list =
                        ((SearchListAdapter)
                                Objects.requireNonNull(recyclerView.getAdapter())).searchItemsFull;
                ArrayList<String> ca_list = new ArrayList<String>();

                for(ZooData.VertexInfo vinfo : vi_list)
                    if(vinfo.checked)
                        ca_list.add(vinfo.name);

                planIntent.putExtra("checked_animals", ca_list);
                startActivity(planIntent);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Filters the list of animals based on the search bar
                Log.d("SearchActivity", animalList.get(0).toString());
                filter(editable);
                adapter.notifyDataSetChanged();
            }
        });

        //called when X button is clicked
        //clears the search bar
        deleteSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.setText("");
            }
        });
    }
    public void setPlanCount(int count){
        // Sets the number of animals in the plan
        if (count == 0) {
            planButton.setEnabled(false);
        }
        else {
            planButton.setEnabled(true);
        }
        planButton.setText("Plan(" + count + ")");
    }

    //this filters the search items by checking the input string with animals' name and tags
    public List<ZooData.VertexInfo> filter(Editable editable) {
        // Filters the list of animals based on the search bar so that only the animals that match
        // the search bar or tags are displayed
        List<ZooData.VertexInfo> newSearchItems = new ArrayList<>();
        noSearchResults.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        if (editable.toString().isEmpty() || (editable.toString().trim().equals(""))) {
            adapter.setSearchItems(animalList);
            newSearchItems = animalList;
        } else {
            String newText = editable.toString().toLowerCase();
            for (int i = 0; i < animalList.size(); ++i) {
                if (animalList.get(i).name.toLowerCase().contains(newText)) {
                    newSearchItems.add(animalList.get(i));
                    continue;
                } else {
                    for (int jindex = 0; jindex < animalList.get(i).tags.size(); jindex++) {
                        if (animalList.get(i).tags.get(jindex).equals
                                (editable.toString().toLowerCase())) {
                            newSearchItems.add(animalList.get(i));
                        }
                    }
                }
            }
//            recyclerView.setAdapter(new SearchListAdapter(newSearchItems));
            adapter.setSearchItems(newSearchItems);
        }
        adapter.notifyDataSetChanged();

        //((SearchListAdapter)recyclerView.getAdapter()).setSas(new SAStorage(this));


        if (newSearchItems.isEmpty()) {
            displayNoSearchResults();
        }

        return newSearchItems;
    }

    public List<ZooData.VertexInfo> checkedAnimals() {
        List<ZooData.VertexInfo> checkedAnimals = new ArrayList<>();
        for (int i = 0; i < animalList.size(); i++) {
            if (animalList.get(i).checked) {
                checkedAnimals.add(animalList.get(i));
            }
        }
        return checkedAnimals;
    }

    public void displayNoSearchResults() {
        recyclerView.setVisibility(View.INVISIBLE);
        noSearchResults.setVisibility(View.VISIBLE);
    }
}