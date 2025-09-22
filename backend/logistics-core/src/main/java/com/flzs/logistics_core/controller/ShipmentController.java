package com.flzs.logistics_core.controller;

import com.flzs.logistics_core.model.request.ShipmentRequest;
import com.flzs.logistics_core.model.response.ShipmentResponse;
import com.flzs.logistics_core.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Main controller for shipment operations.
 * Exposes an endpoint to calculate the cost and info for a shipment
 * based on the origin address, destination, and package details.
 */
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    /**
     * Calculates the shipment info, including distance, estimated duration, and total price.
     *
     * @param shipmentRequest JSON with origin, destination, and package data.
     * @return Response with shipment details.
     * @status 200 OK - Shipment calculated successfully.
     * @status 400 Bad Request - Invalid data (e.g., missing or incorrect fields).
     */
    @PostMapping
    public ResponseEntity<ShipmentResponse> calculateShipment(@Valid @RequestBody ShipmentRequest shipmentRequest) {
        ShipmentResponse response = shipmentService.calculateShipment(shipmentRequest);
        return ResponseEntity.ok(response);
    }

}
