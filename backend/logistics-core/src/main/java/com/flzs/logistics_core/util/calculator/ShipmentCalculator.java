package com.flzs.logistics_core.util.calculator;

import com.flzs.logistics_core.model.domain.PackageDetails;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.flzs.logistics_core.util.constants.ShipmentParams.*;

@UtilityClass
public class ShipmentCalculator {

    /**
     * Estimates delivery time in hours based on the travel distance.
     *
     * @param distanceKm The distance of the shipment in kms.
     * @return The estimated delivery time in hours.
     */
    public static int estimateTime(double distanceKm) {
        if (distanceKm < 100) return SHORT_DISTANCE_HOURS;
        if (distanceKm < 500) return MEDIUM_DISTANCE_HOURS;
        return LONG_DISTANCE_HOURS;
    }

    /**
     * Estimates shipment price based on distance, weight, and package volume.
     * Uses tiered pricing model with base cost plus distance, weight, and volumetric charges.
     *
     * @param distanceKm Total shipping distance in kilometers
     * @param pkg        Package details including dimensions and weight
     * @return Calculated shipping price rounded to 2 decimal places
     */
    public static BigDecimal estimatePrice(double distanceKm, PackageDetails pkg) {
        double volume = pkg.getHeightCm() * pkg.getWidthCm() * pkg.getLengthCm();
        double price = PRICE_BASE + (distanceKm * PRICE_PER_KM)
                + (pkg.getWeightKg() * PRICE_PER_KG)
                + (volume * PRICE_PER_CM3);

        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }
}
