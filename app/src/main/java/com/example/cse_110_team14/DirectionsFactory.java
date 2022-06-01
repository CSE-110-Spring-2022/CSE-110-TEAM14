package com.example.cse_110_team14;
public class DirectionsFactory implements DirectionsFactoryInterface {
    public DirectionsInterface getDirectionsStrategy(String strategy) {
        if (strategy.equals("brief")) {
            return new BriefDirections();
        } else if (strategy.equals("detailed")) {
            return new DetailedDirections();
        }
        return null;
    }
}
