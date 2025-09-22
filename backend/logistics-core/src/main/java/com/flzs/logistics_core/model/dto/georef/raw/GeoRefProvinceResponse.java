package com.flzs.logistics_core.model.dto.georef.raw;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeoRefProvinceResponse(@JsonProperty("provincias") List<Province> provinces) {
    public record Province(
            String id,
            @JsonProperty("nombre") String name) {
    }
}
