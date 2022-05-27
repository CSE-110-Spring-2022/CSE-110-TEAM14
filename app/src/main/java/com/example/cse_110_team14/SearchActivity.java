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

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


// This is where you can search and select what animals you want to visit.
public class SearchActivity extends AppCompatActivity{

    public RecyclerView recyclerView;
    public EditText searchBar;
    public SearchListAdapter adapter;
    public List<ZooData.VertexInfo> exhibitList;
    public ImageButton deleteSearchBarBtn;
    public Button planBtn;
    public TextView noSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        Intent planIntent = new Intent(this, PlanActivity.class);

        // Map from vertex ids to vertex objects
        Map<String, ZooData.VertexInfo> vertexInfoMap =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        //List of vertexes
        List<ZooData.VertexInfo> vertexList = new ArrayList<>(vertexInfoMap.values());
        // List of exhibits
        exhibitList = new ArrayList<>();
        // Filter out non-exhibit vertexes
        for (ZooData.VertexInfo vertex : vertexList) {
            if (vertex.kind.toString().equals("EXHIBIT")) {
                exhibitList.add(vertex);
            }
        }

        //Adapter initialization
        adapter = new SearchListAdapter(exhibitList);
        adapter.setHasStableIds(true);
        adapter.setSearchItems(exhibitList);




        ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
        List<CheckedName> checkedNames = new ArrayList<>();
        int checkCount = 0;
        if(itemsDao != null) {
             try{checkedNames = itemsDao.getAll();}catch(Exception e){}

            for (var an : exhibitList) {
                boolean found = false;
                for (var ch : checkedNames) {
                    if (an.name.equals(ch.name)) {
                        found = true;
                        checkCount++;
                        break;
                    }
                }
                System.out.println(found);
                an.checked = found;
            }
            adapter.itemsDao = itemsDao;
            adapter.notifyDataSetChanged();
        }

        recyclerView = findViewById(R.id.search_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        searchBar = findViewById(R.id.search_text);
        deleteSearchBarBtn = findViewById(R.id.delete_btn);
        planBtn = findViewById(R.id.plan_button);
        planBtn.setText("Plan(0)");
        planBtn.setEnabled(false);

        if(checkedNames.size() > 0) {
            setPlanCount(checkCount);
        }
        noSearchResults = findViewById(R.id.no_search_results);
        noSearchResults.setVisibility(View.INVISIBLE);

        //Add dividers between recyclerView items
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setSas(new SAStorage(this));

        //called when the search bar is being edited
        planBtn.setOnClickListener(new View.OnClickListener(){
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
                Log.d("SearchActivity", exhibitList.get(0).toString());
                filter(editable);
                adapter.notifyDataSetChanged();
            }
        });

        //called when X button is clicked
        //clears the search bar
        deleteSearchBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.setText("");
            }
        });
    }
    public void setPlanCount(int count){
        // Sets the number of animals in the plan
        if (count == 0) {
            planBtn.setEnabled(false);
        }
        else {
            planBtn.setEnabled(true);
        }
        planBtn.setText("Plan(" + count + ")");
    }

    //this filters the search items by checking the input string with animals' name and tags
    public List<ZooData.VertexInfo> filter(Editable editable) {
        // Filters the list of animals based on the search bar so that only the animals that match
        // the search bar or tags are displayed
        List<ZooData.VertexInfo> newSearchItems = new ArrayList<>();
        noSearchResults.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        if (editable.toString().isEmpty() || (editable.toString().trim().equals(""))) {
            adapter.setSearchItems(exhibitList);
            newSearchItems = exhibitList;
        } else {
            String newText = editable.toString().toLowerCase();
            for (int i = 0; i < exhibitList.size(); ++i) {
                if (exhibitList.get(i).name.toLowerCase().contains(newText)) {
                    newSearchItems.add(exhibitList.get(i));
                    continue;
                } else {
                    for (int jindex = 0; jindex < exhibitList.get(i).tags.size(); jindex++) {
                        if (exhibitList.get(i).tags.get(jindex).equals
                                (editable.toString().toLowerCase())) {
                            newSearchItems.add(exhibitList.get(i));
                        }
                    }
                }
            }
            adapter.setSearchItems(newSearchItems);
        }
        adapter.notifyDataSetChanged();

        if (newSearchItems.isEmpty()) {
            displayNoSearchResults();
        }

        return newSearchItems;
    }

    public List<ZooData.VertexInfo> checkedAnimals() {
        List<ZooData.VertexInfo> checkedAnimals = new ArrayList<>();
        for (int i = 0; i < exhibitList.size(); i++) {
            if (exhibitList.get(i).checked) {
                checkedAnimals.add(exhibitList.get(i));
            }
        }
        return checkedAnimals;
    }

    public void displayNoSearchResults() {
        recyclerView.setVisibility(View.INVISIBLE);
        noSearchResults.setVisibility(View.VISIBLE);
    }
}