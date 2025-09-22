package com.flzs.logistics_core.model.dto.geoapify;

import com.flzs.logistics_core.model.domain.Coordinates;
import com.flzs.logistics_core.model.enums.AddressAccuracy;

public record ResolvedLocation(
        Coordinates coordinates,
        AddressAccuracy accuracy,
        double confidence,
        String matchType,
        String formatted,
        double distanceFromCenterMeters
) {
}