package com.example.cse_110_team14;

import android.app.Application;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitExhibitModel extends AndroidViewModel {
    private final MutableLiveData<Pair<Double, Double>> lastKnownCoords;
    private final Map<String, Pair<Double,Double>> LatsAndLngs = new HashMap<>();
    private ZooData.VertexInfo currExhibit;

    public VisitExhibitModel(@NonNull Application application) {
        super(application);
        lastKnownCoords = new MutableLiveData<>(null);
    }

    public void setCurrExhibit(ZooData.VertexInfo exhibit) {
        currExhibit = exhibit;
    }

    public void setLastKnownCoords(Pair<Double, Double> coords) {
        Log.d("setCoords", "Latitude: "+ coords.first);
        Log.d("setCoords", "Longitude: " + coords.second);
        lastKnownCoords.setValue(coords);
    }

    public LiveData<Pair<Double, Double>> getLastKnownCoords() {
        return lastKnownCoords;
    }

    public void setLatsAndLngs(List<ZooData.VertexInfo> exhibitList) {
        for (ZooData.VertexInfo exhibit : exhibitList) {
            LatsAndLngs.put(exhibit.name, new Pair<>(exhibit.lat, exhibit.lng));
        }

        for (Map.Entry<String,Pair<Double,Double>> entry: LatsAndLngs.entrySet()) {
            Log.d("LatLngCheck", "Exhibit:" + entry.getKey() +
                    ", Lat: " + entry.getValue().first +
                    ", Lng: " + entry.getValue().second);
        }
    }

    public boolean checkOffRoute() {
        if (closeToCurrExhibit()) {
            return false;
        }

        for (Map.Entry<String,Pair<Double,Double>> entry : LatsAndLngs.entrySet()) {
            if (closeTo(entry.getValue())) {
                return true;
            }
        }

        return false;
    }

    public boolean closeToCurrExhibit() {
        if (LatsAndLngs.get(currExhibit) != null) {
            return closeCheck(lastKnownCoords.getValue(), LatsAndLngs.get(currExhibit));
        }
        return true;
    }

    public boolean closeTo(Pair<Double,Double> otherCoord)  {
        return closeCheck(lastKnownCoords.getValue(), otherCoord);
    }

    public boolean closeCheck(Pair<Double,Double> coord1, Pair<Double,Double> coord2) {
        var LatDifference = coord1.first - coord2.first;
        var LngDifference = coord1.second - coord2.second;
        return Math.sqrt(Math.pow(LatDifference, 2) + Math.pow(LngDifference, 2)) < 0.001;
    }

}
