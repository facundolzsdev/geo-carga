package com.flzs.logistics_core.util.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UiMessages {

    public static final String ERROR_MUNICIPALITY_NOT_FOUND =
            "Lo sentimos. Actualmente no tenemos información sobre la localidad: %s, %s, Argentina.";

    public static final String ERROR_SERVICE_UNAVAILABLE =
            "Servicio externo no disponible. Por favor, intente más tarde.";

    public static final String ERROR_ROUTE_CALCULATION = "No se pudo calcular la ruta solicitada.";

    public static final String ERROR_LOCATION_NOT_FOUND = "No se pudo encontrar la dirección especificada.";

    public static final String ERROR_EXTERNAL_SERVICE =
            "Error en servicio externo. Por favor, intente más tarde.";
}