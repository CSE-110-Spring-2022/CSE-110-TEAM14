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
    private ZooData.VertexInfo currExhibitDisplayed;
    private List<ZooData.VertexInfo> futureExhibits;

    public VisitExhibitModel(@NonNull Application application) {
        super(application);
        lastKnownCoords = new MutableLiveData<>(null);
    }

    public void setCurrExhibitDisplayed(ZooData.VertexInfo currExhibitDisplayed) {
        this.currExhibitDisplayed = currExhibitDisplayed;
    }

    public void setFutureExhibits(List<ZooData.VertexInfo> futureExhibits) {
        this.futureExhibits = futureExhibits;
    }

    public void setLastKnownCoords(Pair<Double, Double> coords) {
        Log.d("setCoords", "Latitude: "+ coords.first);
        Log.d("setCoords", "Longitude: " + coords.second);
        Log.d("setCoords", "------------------------------");
        lastKnownCoords.setValue(coords);
    }

    public LiveData<Pair<Double, Double>> getLastKnownCoords() {
        return lastKnownCoords;
    }

    public void setLatsAndLngs(List<ZooData.VertexInfo> exhibitList) {
        for (ZooData.VertexInfo exhibit : exhibitList) {
            LatsAndLngs.put(exhibit.id, Pair.create(exhibit.lat, exhibit.lng));
        }

        for (Map.Entry<String,Pair<Double,Double>> entry : LatsAndLngs.entrySet()) {
            Log.d("LatLngCheck", "Exhibit:" + entry.getKey() +
                    ", Lat: " + entry.getValue().first +
                    ", Lng: " + entry.getValue().second);
        }
    }

    public Pair<Boolean,String> checkOffRoute() {
        if ((lastKnownCoords == null) || (LatsAndLngs.isEmpty())) {
            return new Pair<>(false, "");
        }

        if (exhibitDisplayedisLast()) {
            return new Pair<>(false, "");
        }

        double distFromLastKnownCoordsToExhibit =
                getPathLength(lastKnownCoords.getValue(), LatsAndLngs.get(currExhibitDisplayed.id));
        for (ZooData.VertexInfo futureExhibit : futureExhibits) {
            if (getPathLength(lastKnownCoords.getValue(), LatsAndLngs.get(futureExhibit.id)) < distFromLastKnownCoordsToExhibit) {
                return new Pair<>(true, futureExhibit.name);
            }
        }

        return new Pair<>(false, "");
    }

    public double getPathLength(Pair<Double, Double> coord1, Pair<Double, Double> coord2) {
        Log.e("getPathLength", "Lat1: " + coord1 + ", Lng1: " + coord2);
        var LatDifference = coord1.first - coord2.first;
        var LngDifference = coord1.second - coord2.second;
        return Math.sqrt(Math.pow(LatDifference, 2) + Math.pow(LngDifference, 2));
    }

    public boolean exhibitDisplayedisLast() {
        return futureExhibits.size() == 0;
    }

    // returns closest vertex id from last known location
    public String getClosestVertex() {
        try {
            double shortestDistance = Double.MAX_VALUE;
            String closestVertex = "";
            for (Map.Entry<String, Pair<Double, Double>> entry : LatsAndLngs.entrySet()) {
                double entryDistance = getPathLength(lastKnownCoords.getValue(), entry.getValue());
                if (entryDistance < shortestDistance) {
                    shortestDistance = entryDistance;
                    closestVertex = entry.getKey();
                }
            }
            return closestVertex;
        }
        catch (Exception e) {
            Log.d("closest", "Error: " + e.getMessage());
            return "gorilla";
        }
    }
}
