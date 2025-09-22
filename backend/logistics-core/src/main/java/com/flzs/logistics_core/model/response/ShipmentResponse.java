package com.flzs.logistics_core.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flzs.logistics_core.model.enums.AddressAccuracy;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShipmentResponse {

    @JsonProperty("distanciaKm")
    private double distanceKm;

    @JsonProperty("tiempoEstimadoHoras")
    private int estimatedHours;

    @JsonProperty("precioEstimado")
    private BigDecimal estimatedPrice;

    @JsonProperty("instrucciones")
    private List<String> instructions;

    @JsonProperty("datosMapa")
    private MapData mapData;

    private AddressAccuracy originAccuracy;
    private String originAccuracyMessage;

    private AddressAccuracy destinationAccuracy;
    private String destinationAccuracyMessage;

}
