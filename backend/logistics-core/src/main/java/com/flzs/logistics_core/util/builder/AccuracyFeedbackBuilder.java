package com.flzs.logistics_core.util.builder;

import com.flzs.logistics_core.model.dto.geoapify.ResolvedLocation;
import com.flzs.logistics_core.model.enums.AddressAccuracy;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccuracyFeedbackBuilder {

    private static final String APPROXIMATE_ORIGIN_ADDRESS_MESSAGE =
            "El origen localizado tiene una aproximación de %.0f metros al punto esperado.";
    private static final String APPROXIMATE_DES_ADDRESS_MESSAGE =
            "El destino localizado tiene una aproximación de %.0f metros al punto esperado.";
    private static final String FALLBACK_ORIGIN_ADDRESS_MESSAGE =
            "No se pudo localizar el origen. Por favor, revise la dirección o intente con una dirección cercana.";
    private static final String FALLBACK_DES_ADDRESS_MESSAGE =
            "No se pudo localizar el destino. Por favor, revise la dirección o intente con una dirección cercana.";

    public static String buildOriginFeedback(ResolvedLocation rl) {
        if (rl == null || rl.accuracy() == AddressAccuracy.FALLBACK) {
            return FALLBACK_ORIGIN_ADDRESS_MESSAGE;
        }

        if (rl.accuracy() == AddressAccuracy.APPROXIMATE && rl.distanceFromCenterMeters() >= 0) {
            return String.format(APPROXIMATE_ORIGIN_ADDRESS_MESSAGE, rl.distanceFromCenterMeters());
        }

        return FALLBACK_ORIGIN_ADDRESS_MESSAGE;
    }

    public static String buildDestinationFeedback(ResolvedLocation rl) {
        if (rl == null || rl.accuracy() == AddressAccuracy.FALLBACK) {
            return FALLBACK_DES_ADDRESS_MESSAGE;
        }

        if (rl.accuracy() == AddressAccuracy.APPROXIMATE && rl.distanceFromCenterMeters() >= 0) {
            return String.format(APPROXIMATE_DES_ADDRESS_MESSAGE, rl.distanceFromCenterMeters());
        }

        return FALLBACK_DES_ADDRESS_MESSAGE;
    }

}
