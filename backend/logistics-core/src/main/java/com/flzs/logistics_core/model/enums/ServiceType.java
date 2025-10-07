package com.flzs.logistics_core.model.enums;

import lombok.Getter;

@Getter
public enum ServiceType {
    NORMAL(1.0, 1.0),
    EXPRESS(1.25, 0.8),
    PRIORITARIO(1.5, 0.6);

    private final double priceMultiplier;
    private final double timeMultiplier;

    ServiceType(double priceMultiplier, double timeMultiplier) {
        this.priceMultiplier = priceMultiplier;
        this.timeMultiplier = timeMultiplier;
    }

    public static ServiceType fromString(String type) {
        if (type == null) return NORMAL;

        try {
            return ServiceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NORMAL;
        }
    }
}