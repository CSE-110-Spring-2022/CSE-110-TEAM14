package com.example.cse_110_team14;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class VisitExhibitModel extends AndroidViewModel {
    private final MutableLiveData<Pair<Double, Double>> lastKnownCoords;

    public VisitExhibitModel(@NonNull Application application) {
        super(application);
        lastKnownCoords = new MutableLiveData<>(null);
    }

    public void setLastKnownCoords(Pair<Double, Double> coords) {
        Log.d("setCoords", "Latitude: "+ coords.first);
        Log.d("setCoords", "Longitude: " + coords.second);
        lastKnownCoords.setValue(coords);
    }

    public LiveData<Pair<Double, Double>> getLastKnownCoords() {
        return lastKnownCoords;
    }
}
