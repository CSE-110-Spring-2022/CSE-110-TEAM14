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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


// This is where you can search and select what animals you want to visit.
public class SearchActivity extends AppCompatActivity{

    public RecyclerView searchRecyclerView;
    public RecyclerView selectedRecyclerView;
    public EditText searchBar;
    public SearchListAdapter searchListAdapter;
    public SelectedListAdapter selectedListAdapter;
    public List<ZooData.VertexInfo> exhibitList;
    public List<String> selectedList;
    public ImageButton deleteSearchBarBtn;
    public Button planBtn;
    public Button clearSelectedListBtn;
    public TextView noSearchResults;
    public TextView noSelectedExhibits;

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
        searchListAdapter = new SearchListAdapter(exhibitList);
        searchListAdapter.setHasStableIds(true);
        searchListAdapter.setSearchItems(exhibitList);

        ItemsDao itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
        List<CheckedName> checkedNames = new ArrayList<>();
        int checkCount = 0;
        if(itemsDao != null) {
            try{checkedNames = itemsDao.getAll();} catch(Exception e){}

            for (var an : exhibitList) {
                boolean found = false;
                for (var ch : checkedNames) {
                    if (an.name.equals(ch.name)) {
                        found = true;
                        checkCount++;
                        break;
                    }
                }
                an.checked = found;
            }
            searchListAdapter.itemsDao = itemsDao;
            searchListAdapter.notifyDataSetChanged();
        }

        selectedList = new ArrayList<>();
        for (CheckedName name : checkedNames) {
            selectedList.add(name.name);
        }
        selectedListAdapter = new SelectedListAdapter(selectedList);
        selectedListAdapter.setHasStableIds(true);
        selectedListAdapter.setSelectedItems(selectedList);

        searchRecyclerView = findViewById(R.id.search_items);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchRecyclerView.setLayoutManager(layoutManager1);
        searchRecyclerView.setAdapter(searchListAdapter);

        selectedRecyclerView = findViewById(R.id.selected_items);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        selectedRecyclerView.setLayoutManager(layoutManager2);
        selectedRecyclerView.setAdapter(selectedListAdapter);

        searchBar = findViewById(R.id.search_text);
        deleteSearchBarBtn = findViewById(R.id.delete_btn);
        planBtn = findViewById(R.id.plan_button);
        planBtn.setText("Plan(0)");
        planBtn.setEnabled(false);
        clearSelectedListBtn = findViewById(R.id.clear_selected_list_btn);

        noSearchResults = findViewById(R.id.no_search_results);
        noSearchResults.setVisibility(View.INVISIBLE);
        noSelectedExhibits = findViewById(R.id.no_selected_exhibits);
        noSelectedExhibits.setVisibility(View.VISIBLE);

        if(checkedNames.size() > 0) {
            setPlanCount(checkCount);
            hideNoSelectedExhibits();
        }

        //Add dividers between recyclerView items
        DividerItemDecoration dividerItemDecoration1 =
                new DividerItemDecoration(searchRecyclerView.getContext(),layoutManager1.getOrientation());
        searchRecyclerView.addItemDecoration(dividerItemDecoration1);

        DividerItemDecoration dividerItemDecoration2 =
                new DividerItemDecoration(selectedRecyclerView.getContext(),layoutManager2.getOrientation());
        selectedRecyclerView.addItemDecoration(dividerItemDecoration2);

        searchListAdapter.setSas(new SAStorage(this));

        //called when the search bar is being edited
        planBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Go to next activity and pass the list of animals they want to visit
                List<ZooData.VertexInfo> vi_list =
                        ((SearchListAdapter)
                                Objects.requireNonNull(searchRecyclerView.getAdapter())).searchItemsFull;
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
                searchListAdapter.notifyDataSetChanged();
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

        clearSelectedListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("sizecheck", ""+exhibitList.size());
                for (ZooData.VertexInfo exhibit : exhibitList) {
                    if (exhibit.checked) {
                        try{itemsDao.delete(exhibit.name);}catch(Exception e){}
                    }
                    exhibit.checked = false;
                }
                searchListAdapter.setSearchItems(exhibitList);
                selectedList.clear();
                selectedListAdapter.setSelectedItems(selectedList);
                searchBar.setText(searchBar.getText().toString());

                planBtn.setText("Plan(0)");
                displayNoSelectedExhibits();
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
        if (editable.toString().isEmpty() || (editable.toString().trim().equals(""))) {
            searchListAdapter.setSearchItems(exhibitList);
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
            searchListAdapter.setSearchItems(newSearchItems);
        }
        searchListAdapter.notifyDataSetChanged();

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
        noSearchResults.setVisibility(View.VISIBLE);
    }

    public void updateSelectedList(ZooData.VertexInfo exhibit) {
        if (selectedList.contains(exhibit.name)) {
            selectedList.remove(exhibit.name);
        }
        else {
            selectedList.add(exhibit.name);
        }
        selectedListAdapter.setSelectedItems(selectedList);
        if (noSelectedExhibits()) {
            displayNoSelectedExhibits();
        }
        else {
            hideNoSelectedExhibits();
        }
    }

    public boolean noSelectedExhibits() {
        if (selectedList.isEmpty()) {
            return true;
        }
        return false;
    }

    public void displayNoSelectedExhibits() {
        noSelectedExhibits.setVisibility(View.VISIBLE);
    }

    public void hideNoSelectedExhibits() {
        noSelectedExhibits.setVisibility(View.INVISIBLE);
    }

}