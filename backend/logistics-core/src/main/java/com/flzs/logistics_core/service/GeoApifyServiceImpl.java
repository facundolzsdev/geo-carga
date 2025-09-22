package com.flzs.logistics_core.service;

import com.flzs.logistics_core.client.GeoApifyClient;
import com.flzs.logistics_core.model.domain.Coordinates;
import com.flzs.logistics_core.model.dto.geoapify.ResolvedLocation;
import com.flzs.logistics_core.model.routing.RouteInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GeoApifyServiceImpl implements GeoApifyService {

    private final GeoApifyClient client;

    @Override
    public RouteInfo getRoute(Coordinates origin, Coordinates destination) {
        return client.getRouteBetweenCoordinates(origin, destination);
    }

    @Override
    public ResolvedLocation resolveOriginAddress(String address, String municipalityName, String provinceName) {
        return client.geocodeAddressWithProximityValidation(address, municipalityName, provinceName);
    }


    @Override
    public ResolvedLocation resolveDestinationAddress(String address, String municipalityName, String provinceName) {
        return client.geocodeAddressWithProximityValidation(address, municipalityName, provinceName);
    }

    @Override
    public Coordinates getOriginCoordinatesFromMunicipality(String municipalityName, String provinceName) {
        return client.getCoordinatesFromMunicipality(municipalityName, provinceName);
    }
}
