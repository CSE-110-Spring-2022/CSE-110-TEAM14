package com.example.cse_110_team14;

import android.util.Pair;

public class VisitExhibitPresenter {
    private final VisitAnimalActivity activity;
    private final VisitExhibitModel model;

    public VisitExhibitPresenter(VisitAnimalActivity activity, VisitExhibitModel model) {
        this.activity = activity;
        this.model = model;
    }

    public void updateLastKnownCoords(Pair<Double, Double> coords) {
        model.setLastKnownCoords(coords);
    }
}
