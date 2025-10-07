package com.flzs.logistics_core.util.calculator;

import com.flzs.logistics_core.model.domain.PackageDetails;
import com.flzs.logistics_core.model.enums.ServiceType;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.flzs.logistics_core.util.constants.ShipmentParams.*;

@UtilityClass
public class ShipmentCalculator {

    /**
     * Estimates delivery time in hours based on distance and service type.
     *
     * @param distanceKm  The distance of the shipment in kms.
     * @param serviceType The type of service selected
     * @return The estimated delivery time in hours.
     */
    public static int estimateTime(double distanceKm, ServiceType serviceType) {
        int baseHours;
        if (distanceKm < 100) baseHours = SHORT_DISTANCE_HOURS;
        else if (distanceKm < 500) baseHours = MEDIUM_DISTANCE_HOURS;
        else baseHours = LONG_DISTANCE_HOURS;

        return (int) Math.round(baseHours * serviceType.getTimeMultiplier());
    }

    /**
     * Estimates shipment price based on distance, weight, volume, and service type.
     *
     * @param distanceKm  Total shipping distance in kilometers
     * @param pkg         Package details including dimensions and weight
     * @param serviceType The type of service selected
     * @return Calculated shipping price rounded to 2 decimal places
     */
    public static BigDecimal estimatePrice(double distanceKm, PackageDetails pkg, ServiceType serviceType) {
        double volume = pkg.getHeightCm() * pkg.getWidthCm() * pkg.getLengthCm();
        double basePrice = PRICE_BASE + (distanceKm * PRICE_PER_KM)
                + (pkg.getWeightKg() * PRICE_PER_KG)
                + (volume * PRICE_PER_CM3);

        double finalPrice = basePrice * serviceType.getPriceMultiplier();

        return BigDecimal.valueOf(finalPrice).setScale(2, RoundingMode.HALF_UP);
    }

}
