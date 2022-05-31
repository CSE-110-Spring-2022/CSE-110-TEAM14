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

    public void updateCurrExhibitDisplayed
            (ZooData.VertexInfo exhibit, List<ZooData.VertexInfo> futureExhibits) {
        model.setCurrExhibitDisplayed(exhibit);
        model.setFutureExhibits(futureExhibits);
    }

    public void updateLastKnownCoords(Pair<Double, Double> coords) {
        model.setLastKnownCoords(coords);
        model.checkOffRoute();
    }

    public void updateLatsAndLngs(List<ZooData.VertexInfo> exhibitList) {
        model.setLatsAndLngs(exhibitList);
    }
}
