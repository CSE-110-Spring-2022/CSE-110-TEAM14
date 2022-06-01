package com.example.cse_110_team14;

import android.util.Pair;

public interface LocationSubject {
    /**
     * Sets an observer to the subject.
     * @param locationObserver The observer to add.
     */
    void setLocationObserver(LocationObserver locationObserver);

    /**
     * Called when the coords are changed. Should update the observer
     * @param coords The new coords.
     */
    void updateLastKnownCoords(Pair<Double, Double> coords);
}
