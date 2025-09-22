package com.flzs.logistics_core.model.routing;

import java.util.List;

/**
 * Encapsulates routing info between two geographic points.
 *
 * @param instructions List of step-by-step driving instructions (in plain text).
 */
public record RouteInfo(
        double distanceMeters, double timeSeconds,
        List<String> instructions, List<List<Double>> routeCoordinates
) {
    public double distanceInKm() {
        return distanceMeters / 1000.0;
    }
}