package com.flzs.logistics_core.model.domain;

/**
 * Represents geographic coordinates.
 *
 * @param latitude  Latitude in decimal degrees.
 * @param longitude Longitude in decimal degrees.
 */
public record Coordinates(double latitude, double longitude) {
}
