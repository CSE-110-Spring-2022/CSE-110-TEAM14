package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
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

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public EditText searchBar;
    public SearchListAdapter adapter;
    public List<ZooData.VertexInfo> animalList;
    public Button planButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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

        planButton = findViewById(R.id.plan_button);
        planButton.setText("Plan(0)");

        adapter.setSearchItems(animalList);

        planButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("ive been touched");
                setContentView(R.layout.activity_plan);
                //Intent intent = new Intent(SearchActivity.this, PlanActivity.class);
                //startActivity(intent);
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
                Log.d("SearchActivity", animalList.get(0).toString());
                filter(editable);
                adapter.notifyDataSetChanged();
            }
        });

    }

    //this filters the search items by checking the input string with animals' name and tags
    private void filter(Editable editable) {
        List<ZooData.VertexInfo> newSearchItems = new ArrayList<>();
        if (editable.toString().isEmpty() || (editable.toString().trim().equals(""))) {
            recyclerView.setAdapter(new SearchListAdapter(animalList));
            adapter.notifyDataSetChanged();
        } else {
            String newText = editable.toString().toLowerCase();
            for (int index = 0; index < animalList.size(); ++index) {
                if (animalList.get(index).name.toLowerCase().contains(newText)) {
                    newSearchItems.add(animalList.get(index));
                    continue;
                } else {
                    for (int jindex = 0; jindex < animalList.get(index).tags.size(); jindex++) {
                        if (animalList.get(index).tags.get(jindex).equals
                                (editable.toString().toLowerCase())) {
                            newSearchItems.add(animalList.get(index));
                        }
                    }
                }
            }
//            for(int index = 0; index < animalList.size(); ++index){
//                for(int jindex = 0; jindex < animalList.get(index).tags.size(); jindex++) {
//                    if(animalList.get(index).tags.get(jindex).contains(editable.toString().toLowerCase())) {
//                        newSearchItems.add(animalList.get(index));
//                    }
//                }
//            }
            recyclerView.setAdapter(new SearchListAdapter(newSearchItems));
            adapter.notifyDataSetChanged();
        }
    }
}