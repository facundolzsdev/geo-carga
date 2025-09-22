package com.flzs.logistics_core.model.dto.geoapify;

import java.util.List;

/**
 * Handle GeoApify's routing API response.
 *
 * @param features List of route features, each containing route properties.
 */
public record GeoApifyRouteResponse(List<Feature> features) {

    /**
     * Wrapper for route properties and geometry.
     *
     * @param properties Detailed routing info such as distance, time, and steps.
     * @param geometry   Route geometry containing the path coordinates.
     */
    public record Feature(Properties properties, Geometry geometry) {
    }

    /**
     * Contains the route path geometry.
     *
     * @param type        Geometry type (typically "LineString" for routes).
     * @param coordinates Raw coordinate structure from GeoApify (can be nested).
     */
    public record Geometry(String type, Object coordinates) {
    }

    /**
     * Contains overall distance, estimated time, and detailed route legs.
     *
     * @param distance Total distance in meters.
     * @param time     Total duration in seconds.
     * @param legs     List of route segments ("legs"), each with step-by-step instructions.
     */
    public record Properties(double distance, double time, List<Leg> legs) {
    }

    /**
     * Represents a segment of the route with steps.
     *
     * @param steps List of steps for navigation.
     */
    public record Leg(List<Step> steps) {
    }

    /**
     * Represents a navigational step within a leg.
     *
     * @param instruction Instruction details for this step.
     */
    public record Step(Instruction instruction) {
    }

    /**
     * Contains plain text instruction like "Turn right onto Main Street...".
     *
     * @param text Step-by-step instruction text.
     */
    public record Instruction(String text) {
    }
}