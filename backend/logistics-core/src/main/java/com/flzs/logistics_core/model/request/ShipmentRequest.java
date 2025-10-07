package com.flzs.logistics_core.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flzs.logistics_core.model.domain.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShipmentRequest {

    @JsonProperty("origen")
    private Address origin;

    @JsonProperty("destino")
    private Address destination;

    @JsonProperty("paquete")
    private PackageDetails packageDetails;

    @JsonProperty("tipoServicio")
    private String serviceType;

}
