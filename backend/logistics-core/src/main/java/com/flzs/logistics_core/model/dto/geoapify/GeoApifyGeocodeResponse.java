package com.flzs.logistics_core.model.dto.geoapify;

import java.util.List;

/**
 * Handle GeoApify's geocoding API response.
 *
 * @param features List of geolocation results.
 */
public record GeoApifyGeocodeResponse(List<Feature> features) {

    /**
     * Each feature contains a geometry object with the coordinates.
     *
     * @param geometry Geometry data of the location.
     */
    public record Feature(Geometry geometry) {
    }

    /**
     * Geometry info including the list of coordinates (longitude, latitude).
     *
     * @param coordinates Coordinates as [lon, lat].
     */
    public record Geometry(List<Double> coordinates) {
    }
}