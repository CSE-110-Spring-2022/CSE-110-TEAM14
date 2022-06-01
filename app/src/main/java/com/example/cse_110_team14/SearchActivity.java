package com.example.cse_110_team14;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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


// This is where you can search and select what animals you want to visit.
public class SearchActivity extends AppCompatActivity {

    public RecyclerView searchRecyclerView;
    public RecyclerView selectedRecyclerView;
    public EditText searchBar;
    public ImageButton deleteSearchBarBtn;
    public Button planBtn;
    public Button clearSelectedListBtn;
    public TextView noSearchResults;
    public TextView noSelectedExhibits;

    public SearchListAdapter searchListAdapter;
    public SelectedListAdapter selectedListAdapter;
    public ItemsDao itemsDao;

    public SearchPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent planIntent = new Intent(this, PlanActivity.class);

        // initialize model and presenter
        SearchModel model = new ViewModelProvider(this).get(SearchModel.class);
        presenter = new SearchPresenter(this, model);
        Map<String, ZooData.VertexInfo> vertexInfoMap =
                ZooData.loadVertexInfoJSON(this, "zoo_node_info.json");
        model.setVertexList(vertexInfoMap);

        // initialize Search page view
        searchBar = findViewById(R.id.search_text);
        deleteSearchBarBtn = findViewById(R.id.delete_btn);
        planBtn = findViewById(R.id.plan_button);
        setPlanCount(0);
        clearSelectedListBtn = findViewById(R.id.clear_selected_list_btn);
        noSearchResults = findViewById(R.id.no_search_results);
        hideNoSearchResults();
        noSelectedExhibits = findViewById(R.id.no_selected_exhibits);
        displayNoSelectedExhibits();
        initializeSearchRecyclerView();
        int checkCount = loadFromDatabase();
        initializeSelectedRecyclerView();
        setPlanCount(checkCount);

        searchListAdapter.setSas(new SAStorage(this));

        // called as the search bar is edited
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
                Log.d("SearchActivity", presenter.getExhibitList().get(0).toString());
                filter(editable);
            }
        });

        // move to Plan page when Plan button is clicked
        planBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Go to next activity and pass the list of animals they want to visit
                planIntent.putExtra("checked_animals", getCheckedExhibitsNames());
                startActivity(planIntent);
            }
        });

        // clear the search bar when X is clicked
        deleteSearchBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.setText("");
            }
        });

        // clear the selected list and uncheck all exhibits in search list when clear button is clicked
        clearSelectedListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ZooData.VertexInfo> exhibitList = presenter.getExhibitList();
                Log.d("sizecheck", ""+exhibitList.size());
                for (ZooData.VertexInfo exhibit : exhibitList) {
                    if (exhibit.checked) {
                        itemsDao.delete(exhibit.name);
                    }
                    exhibit.checked = false;
                }
                searchListAdapter.setSearchItems(exhibitList);
                presenter.clearSelectedExhibits();
                selectedListAdapter.setSelectedItems(presenter.getSelectedList());
                searchBar.setText(searchBar.getText().toString());

                planBtn.setText("Plan(0)");
                displayNoSelectedExhibits();
            }
        });
    }

    // Show the number of exhibits planned on Plan button
    public void setPlanCount(int count){
        if (count == 0) {
            planBtn.setEnabled(false);
        }
        else {
            hideNoSelectedExhibits();
            planBtn.setEnabled(true);
        }
        planBtn.setText("Plan(" + count + ")");
    }

    // filters the search list based on the search bar
    public List<ZooData.VertexInfo> filter(Editable editable) {
        List<ZooData.VertexInfo> newSearchItems = new ArrayList<>();
        List<ZooData.VertexInfo> exhibitList = presenter.getExhibitList();
        hideNoSearchResults();
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
                    for (int j = 0; j < exhibitList.get(i).tags.size(); j++) {
                        if (exhibitList.get(i).tags.get(j).equals
                                (editable.toString().toLowerCase())) {
                            newSearchItems.add(exhibitList.get(i));
                        }
                    }
                }
            }
            searchListAdapter.setSearchItems(newSearchItems);
        }

        if (newSearchItems.isEmpty()) {
            displayNoSearchResults();
        }

        return newSearchItems;
    }

    public ArrayList<String> getCheckedExhibitsNames() {
        List<ZooData.VertexInfo> exhibitList = presenter.getExhibitList();
        ArrayList<String> checkedExhibits = new ArrayList<>();
        for(ZooData.VertexInfo exhibit : exhibitList) {
            if (exhibit.checked) {
                checkedExhibits.add(exhibit.name);
            }
        }
        return checkedExhibits;
    }

    public List<ZooData.VertexInfo> getCheckedExhibits() {
        List<ZooData.VertexInfo> exhibitList = presenter.getExhibitList();
        List<ZooData.VertexInfo> checkedAnimals = new ArrayList<>();
        for (int i = 0; i < exhibitList.size(); i++) {
            if (exhibitList.get(i).checked) {
                checkedAnimals.add(exhibitList.get(i));
            }
        }
        return checkedAnimals;
    }

    public void updateSelectedRecyclerView(ZooData.VertexInfo exhibit) {
        presenter.updateSelectedExhibit(exhibit);
        selectedListAdapter.setSelectedItems(presenter.getSelectedList());
        if (presenter.noSelectedExhibits()) {
            displayNoSelectedExhibits();
        }
        else {
            hideNoSelectedExhibits();
        }
    }

    // load the checked exhibits from database
    public int loadFromDatabase() {
        itemsDao = ItemsDatabase.getSingleton(this).itemsDao();
        List<ZooData.VertexInfo> exhibitList = presenter.getExhibitList();
        List<CheckedName> checkedNames = new ArrayList<>();
        int checkCount = 0;
        if(itemsDao != null) {
            checkedNames = itemsDao.getAll();
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
        presenter.updateSelectedExhibits(checkedNames);
        return checkCount;
    }

    // initialize the adapter and recycler view for search list
    public void initializeSearchRecyclerView() {
        List<ZooData.VertexInfo> exhibitList = presenter.getExhibitList();
        searchListAdapter = new SearchListAdapter(exhibitList);
        searchListAdapter.setHasStableIds(true);
        searchListAdapter.setSearchItems(exhibitList);
        searchRecyclerView = findViewById(R.id.search_items);
        LinearLayoutManager searchLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        searchRecyclerView.setLayoutManager(searchLayoutManager);
        searchRecyclerView.setAdapter(searchListAdapter);
        addDividerLines(searchRecyclerView, searchLayoutManager);
    }

    // initialize the adapter and recycler view for selected list
    public void initializeSelectedRecyclerView() {
        List<String> selectedList = presenter.getSelectedList();
        selectedListAdapter = new SelectedListAdapter(selectedList);
        selectedListAdapter.setHasStableIds(true);
        selectedListAdapter.setSelectedItems(selectedList);
        selectedRecyclerView = findViewById(R.id.selected_items);
        LinearLayoutManager selectedLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        selectedRecyclerView.setLayoutManager(selectedLayoutManager);
        selectedRecyclerView.setAdapter(selectedListAdapter);
        addDividerLines(selectedRecyclerView, selectedLayoutManager);
    }

    public void displayNoSearchResults() { noSearchResults.setVisibility(View.VISIBLE); }

    public void hideNoSearchResults() { noSearchResults.setVisibility(View.INVISIBLE); }

    public void displayNoSelectedExhibits() { noSelectedExhibits.setVisibility(View.VISIBLE); }

    public void hideNoSelectedExhibits() { noSelectedExhibits.setVisibility(View.INVISIBLE); }

    //Add dividers between recyclerView items
    public void addDividerLines(RecyclerView recyclerView, LinearLayoutManager layoutManager) {
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}