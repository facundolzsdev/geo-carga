package com.flzs.logistics_core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Contains all geographic data needed to render the route map.
 *
 * @param routeCoordinates       Array of [longitude, latitude] coordinate pairs forming the route path.
 * @param originCoordinates      Origin point as [longitude, latitude].
 * @param destinationCoordinates Destination point as [longitude, latitude].
 */
public record MapData(
        @JsonProperty("coordenadasRuta")
        List<List<Double>> routeCoordinates,

        @JsonProperty("coordenadasOrigen")
        List<Double> originCoordinates,

        @JsonProperty("coordenadasDestino")
        List<Double> destinationCoordinates
) {
}