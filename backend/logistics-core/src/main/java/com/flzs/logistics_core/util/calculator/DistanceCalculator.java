package com.flzs.logistics_core.util.calculator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DistanceCalculator {

    /**
     * Calculates the great-circle distance between two geographic points using the Haversine formula.
     * Used for proximity validation during address geocoding.
     *
     * @param lat1 Latitude of first point in decimal degrees
     * @param lon1 Longitude of first point in decimal degrees
     * @param lat2 Latitude of second point in decimal degrees
     * @param lon2 Longitude of second point in decimal degrees
     * @return Distance between points in meters
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radius of Earth in METERS (6371 KM)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in METERS
    }

}
