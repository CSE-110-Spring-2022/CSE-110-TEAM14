package com.example.cse_110_team14;

import android.util.Log;
import android.util.Pair;

import java.util.List;

public class VisitExhibitPresenter implements LocationSubject {
    public LocationObserver locationObserver = null;
    private final VisitAnimalActivity activity;
    private final VisitExhibitModel model;

    public VisitExhibitPresenter(VisitAnimalActivity activity, VisitExhibitModel model) {
        this.activity = activity;
        this.model = model;
    }

    public void setLocationObserver(LocationObserver locationObserver) {
        this.locationObserver = locationObserver;
    }



    public void updateCurrExhibitDisplayed(ZooData.VertexInfo exhibit, List<ZooData.VertexInfo> futureExhibits) {
        model.setCurrExhibitDisplayed(exhibit);
        model.setFutureExhibits(futureExhibits);
    }

    public void updateLastKnownCoords(Pair<Double, Double> coords) {
        model.setLastKnownCoords(coords);
//        Pair<Boolean, String> result = model.checkOffRoute();
//        if (result.first) {
//            if (activity.animalsInOrder.indexOf(result.second) > activity.currIndex &&
//                    activity.animalsInOrder.indexOf(result.second) !=
//                            activity.animalsInOrder.size() - 1) {
//                activity.offRoutePrompt();
//            }
//        }
        int currIndex = activity.currIndex;
        List<String> exhibitIDsInOrder = activity.exhibitIDsInOrder;
        int currDistance = activity.distance(activity.currentLocation(),
                exhibitIDsInOrder.get(currIndex));
        for(int i = currIndex + 1; i < exhibitIDsInOrder.size(); i++) {
            int newDist = activity.distance(activity.currentLocation(), exhibitIDsInOrder.get(i));
            if( newDist < currDistance && currIndex != exhibitIDsInOrder.size() - 2) {
                activity.offRoutePrompt();
                if (locationObserver != null && locationHelper()) {
                    locationObserver.onChange();
                }
                break;
            }
        }

    }

    public boolean locationHelper() {
        return false;
    }

    public void updateLatsAndLngs(List<ZooData.VertexInfo> exhibitList) {
        model.setLatsAndLngs(exhibitList);
    }

    public String getClosestVertex() {
        return model.getClosestVertex();
    }
}
