package com.example.cse_110_team14;

import android.text.Editable;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter {

    private final SearchActivity activity;
    private final SearchModel model;

    public SearchPresenter(SearchActivity activity, SearchModel model) {
        this.activity = activity;
        this.model = model;
    }

    public void updateSelectedExhibits(List<CheckedName> checkedNames) {
        model.setSelectedList(checkedNames);
    }

    public void updateSelectedExhibit(ZooData.VertexInfo exhibit) {
        if (getSelectedList().contains(exhibit.name)) {
            getSelectedList().remove(exhibit.name);
        }
        else {
            getSelectedList().add(exhibit.name);
        }
    }

    public void clearSelectedExhibits() {
        model.clearSelectedList();
    }

    public List<ZooData.VertexInfo> getVertexList() {
        return model.getVertexList();
    }

    public List<ZooData.VertexInfo> getExhibitList() {
        return model.getExhibitList();
    }

    public List<String> getSelectedList() {
        return model.getSelectedList();
    }

    public boolean noSelectedExhibits() {
        return model.noSelectedExhibits();
    }
}
