package com.flzs.logistics_core.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationNotFoundException extends ExternalApiException {

    private String municipality;
    private String province;

    public LocationNotFoundException(String message) {
        super(message);
    }

    public LocationNotFoundException(String municipality, String province) {
        super(String.format("Could not find coordinates for municipality: %s, %s", municipality, province));
        this.municipality = municipality;
        this.province = province;
    }
}
