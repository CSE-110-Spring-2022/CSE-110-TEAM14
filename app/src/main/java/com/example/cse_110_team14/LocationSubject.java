package com.example.cse_110_team14;

import android.util.Pair;

public interface LocationSubject {
    void setLocationObserver(LocationObserver locationObserver);
    void updateLastKnownCoords(Pair<Double, Double> coords);
}
