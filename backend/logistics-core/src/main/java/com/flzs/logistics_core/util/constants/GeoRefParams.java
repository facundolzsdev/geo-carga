package com.flzs.logistics_core.util.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GeoRefParams {

    // Timeouts & Values
    public static final int CONNECT_TIMEOUT_MILLIS = 10000;
    public static final int RESPONSE_TIMEOUT_SECONDS = 30;
    public static final int MAX_RESULTS = 5000;

    // Base URL & Paths
    public static final String BASE_URL = "https://apis.datos.gob.ar/georef/api";
    public static final String PATH_PROVINCES = "/provincias";
    public static final String PATH_DEPARTMENTS = "/departamentos";
    public static final String PATH_MUNICIPALITIES = "/municipios";

    // Query params
    public static final String QUERY_PARAM_PROVINCE = "provincia";
    public static final String QUERY_PARAM_INTERSECTION = "interseccion";
    public static final String QUERY_PARAM_FIELDS = "campos";
    public static final String QUERY_PARAM_MAX = "max";

}
