package com.flzs.logistics_core.service;

import com.flzs.logistics_core.model.domain.*;
import com.flzs.logistics_core.model.dto.geoapify.ResolvedLocation;
import com.flzs.logistics_core.model.enums.AddressAccuracy;
import com.flzs.logistics_core.model.enums.ServiceType;
import com.flzs.logistics_core.model.request.ShipmentRequest;
import com.flzs.logistics_core.model.response.*;
import com.flzs.logistics_core.model.routing.RouteInfo;
import com.flzs.logistics_core.util.calculator.ShipmentCalculator;
import com.flzs.logistics_core.util.builder.AccuracyFeedbackBuilder;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentServiceImpl.class);
    private final GeoApifyService geoApifyService;

    @Override
    public ShipmentResponse calculateShipment(ShipmentRequest shipment) {
        // Step 1: Resolve and validate the origin address
        ResolvedLocation origin = resolveOrigin(shipment.getOrigin());
        if (!isOriginValid(origin)) {
            return buildOriginFallbackResponse(origin);
        }

        // Step 2: Resolve and validate the destination address
        ResolvedLocation destination = resolveDestination(shipment.getDestination());
        if (!isDestinationValid(destination)) {
            return buildDestinationFallbackResponse(destination);
        }

        // Step 3: Calculate route using validated coordinates
        RouteInfo route = geoApifyService.getRoute(origin.coordinates(), destination.coordinates());

        // Step 4: Get service type
        ServiceType serviceType = ServiceType.fromString(shipment.getServiceType());

        logger.info("Calculating shipment with service type: {}", serviceType);

        return buildSuccessfulResponse(route, origin, destination, shipment.getPackageDetails(), serviceType);
    }

    private ResolvedLocation resolveOrigin(Address address) {
        return geoApifyService.resolveOriginAddress(
                formatFullAddress(address),
                address.getMunicipality(),
                address.getProvince()
        );
    }

    private ResolvedLocation resolveDestination(Address address) {
        return geoApifyService.resolveDestinationAddress(
                formatFullAddress(address),
                address.getMunicipality(),
                address.getProvince()
        );
    }

    private boolean isOriginValid(ResolvedLocation origin) {
        boolean isValid = origin.accuracy() == AddressAccuracy.APPROXIMATE;

        logger.info("Origin validation: accuracy={}, confidence={}, distance={}m, valid={}",
                origin.accuracy(), origin.confidence(),
                origin.distanceFromCenterMeters() >= 0 ? String.format("%.0f", origin.distanceFromCenterMeters()) : "N/A",
                isValid);

        return isValid;
    }

    private boolean isDestinationValid(ResolvedLocation destination) {
        boolean isValid = destination.accuracy() == AddressAccuracy.APPROXIMATE;

        logger.info("Destination validation: accuracy={}, confidence={}, distance={}m, valid={}",
                destination.accuracy(), destination.confidence(),
                destination.distanceFromCenterMeters() >= 0 ? String.format("%.0f", destination.distanceFromCenterMeters()) : "N/A",
                isValid);

        return isValid;
    }

    private ShipmentResponse buildOriginFallbackResponse(ResolvedLocation origin) {
        ShipmentResponse resp = new ShipmentResponse();
        resp.setOriginAccuracy(AddressAccuracy.FALLBACK);
        resp.setOriginAccuracyMessage(AccuracyFeedbackBuilder.buildOriginFeedback(origin));
        return resp;
    }

    private ShipmentResponse buildDestinationFallbackResponse(ResolvedLocation destination) {
        ShipmentResponse resp = new ShipmentResponse();
        resp.setDestinationAccuracy(AddressAccuracy.FALLBACK);
        resp.setDestinationAccuracyMessage(AccuracyFeedbackBuilder.buildDestinationFeedback(destination));
        return resp;
    }

    private ShipmentResponse buildSuccessfulResponse(RouteInfo route, ResolvedLocation origin,
                                                     ResolvedLocation destination, PackageDetails pkg,
                                                     ServiceType serviceType) {
        double distanceKm = route.distanceInKm();
        int etaHours = ShipmentCalculator.estimateTime(distanceKm, serviceType);
        BigDecimal price = ShipmentCalculator.estimatePrice(distanceKm, pkg, serviceType);

        ShipmentResponse resp = new ShipmentResponse();
        resp.setDistanceKm(distanceKm);
        resp.setEstimatedHours(etaHours);
        resp.setEstimatedPrice(price);
        resp.setInstructions(route.instructions());

        setAccuracyInfo(resp, origin, destination);
        resp.setMapData(createMapData(route, origin, destination));

        return resp;
    }

    private void setAccuracyInfo(ShipmentResponse resp, ResolvedLocation origin, ResolvedLocation destination) {
        resp.setOriginAccuracy(origin.accuracy());
        resp.setOriginAccuracyMessage(AccuracyFeedbackBuilder.buildOriginFeedback(origin));
        resp.setDestinationAccuracy(destination.accuracy());
        resp.setDestinationAccuracyMessage(AccuracyFeedbackBuilder.buildDestinationFeedback(destination));
    }

    private MapData createMapData(RouteInfo route, ResolvedLocation origin, ResolvedLocation destination) {
        return new MapData(
                route.routeCoordinates(),
                List.of(origin.coordinates().longitude(), origin.coordinates().latitude()),
                List.of(destination.coordinates().longitude(), destination.coordinates().latitude())
        );
    }

    private String formatFullAddress(Address address) {
        return Stream.of(address.getStreetNumber(),
                        address.getStreet(),
                        address.getMunicipality(),
                        address.getProvince(),
                        "Argentina")
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
    }
}