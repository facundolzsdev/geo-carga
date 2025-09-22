package com.flzs.logistics_core.model.dto.georef.raw;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GeoRefMunicipalityResponse(@JsonProperty("municipios") List<Municipality> municipalities) {

    public record Municipality(
            String id,
            @JsonProperty("nombre") String name,
            @JsonProperty("provincia") Province province,
            @JsonProperty("departamento") Department department) {

        public record Province(
                String id,
                @JsonProperty("nombre") String name) {
        }

        public record Department(
                String id,
                @JsonProperty("nombre") String name) {
        }
    }
}
