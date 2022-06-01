package com.example.cse_110_team14;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchModel extends AndroidViewModel {

    //List of vertexes
    private final List<ZooData.VertexInfo> vertexList = new ArrayList<>();
    // List of exhibits
    private final List<ZooData.VertexInfo> exhibitList = new ArrayList<>();
    private List<String> selectedList = new ArrayList<>();

    public SearchModel(@NonNull Application application) {
        super(application);
    }

    public void setVertexList(Map<String, ZooData.VertexInfo> vertexInfoMap) {
        vertexList.addAll(vertexInfoMap.values());
        for (ZooData.VertexInfo vertex : vertexList) {
            if (vertex.kind.toString().equals("EXHIBIT")) {
                exhibitList.add(vertex);
            }
        }
    }

    public void setSelectedList(List<CheckedName> checkedNames) {
        for (CheckedName name : checkedNames) {
            selectedList.add(name.name);
        }
    }

    public void clearSelectedList() {
        selectedList.clear();
    }

    public List<ZooData.VertexInfo> getVertexList() {
        return vertexList;
    }

    public List<ZooData.VertexInfo> getExhibitList() {
        return exhibitList;
    }

    public List<String> getSelectedList() {
        return selectedList;
    }

    public boolean noSelectedExhibits() {
        return selectedList.isEmpty();
    }

}
