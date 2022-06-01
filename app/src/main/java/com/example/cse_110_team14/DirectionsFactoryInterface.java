package com.example.cse_110_team14;

public interface DirectionsFactoryInterface {
    /**
     * Returns a DirectionsInterface object based on the given strategy.
     * @param strategy The strategy to use.
     * @return A DirectionsInterface object based on the given strategy.
     */
    DirectionsInterface getDirectionsStrategy(String strategy);
}
