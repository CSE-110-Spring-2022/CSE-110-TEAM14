package com.example.cse_110_team14;

import android.util.Pair;

import java.util.List;

public class VisitExhibitPresenter {
    private final VisitAnimalActivity activity;
    private final VisitExhibitModel model;

    public VisitExhibitPresenter(VisitAnimalActivity activity, VisitExhibitModel model) {
        this.activity = activity;
        this.model = model;
    }

    public void updateCurrExhibit(ZooData.VertexInfo exhibit) {
        model.setCurrExhibit(exhibit);
    }

    public void updateLastKnownCoords(Pair<Double, Double> coords) {
        model.setLastKnownCoords(coords);
        model.checkOffRoute();
    }

    public void updateLatsAndLngs(List<ZooData.VertexInfo> exhibitList) {
        model.setLatsAndLngs(exhibitList);
    }
}
