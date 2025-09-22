package com.flzs.logistics_core.service;

import com.flzs.logistics_core.model.request.ShipmentRequest;
import com.flzs.logistics_core.model.response.ShipmentResponse;
import jakarta.validation.Valid;

/**
 * Service interface for handling the core shipping calculation logic.
 */
public interface ShipmentService {

    /**
     * Calculates complete shipment information including pricing, delivery time, and route details.
     * Validates both origin and destination addresses through geocoding, then computes
     * the optimal route and associated costs.
     *
     * @param shipment Request containing origin, destination, and package details
     * @return Complete shipment response with pricing, route, and accuracy information
     */
    ShipmentResponse calculateShipment(@Valid ShipmentRequest shipment);

}
