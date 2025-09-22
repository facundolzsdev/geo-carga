package com.flzs.logistics_core.service;

import com.flzs.logistics_core.model.domain.Coordinates;
import com.flzs.logistics_core.model.dto.geoapify.ResolvedLocation;
import com.flzs.logistics_core.model.routing.RouteInfo;

public interface GeoApifyService {

    RouteInfo getRoute(Coordinates origin, Coordinates destination);

    ResolvedLocation resolveOriginAddress(String address, String municipalityName, String provinceName);

    ResolvedLocation resolveDestinationAddress(String address, String municipalityName, String provinceName);

    Coordinates getOriginCoordinatesFromMunicipality(String municipalityName, String provinceName);
}
