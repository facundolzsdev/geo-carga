package com.flzs.logistics_core.util.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GeoApifyParams {

    // Timeouts
    public static final int CONNECT_TIMEOUT_MILLIS = 10000;
    public static final int RESPONSE_TIMEOUT_SECONDS = 30;

    // Distance / thresholds
    public static final int MAX_ACCEPTABLE_DISTANCE_METERS = 2000; // 2 km radius (reasonable for local addresses)
    public static final double CENTER_THRESHOLD_METERS = 1.0;
    public static final double NO_DISTANCE_AVAILABLE = -1.0;
    public static final double HIGH_CONFIDENCE_THRESHOLD = 0.5;

    // Base URL & Endpoints
    public static final String BASE_URL = "https://api.geoapify.com";
    public static final String GEOCODE_PATH = "/v1/geocode/search";
    public static final String ROUTING_PATH = "/v1/routing";

    // Filters / Values
    public static final String COUNTRY_CODE_AR = "countrycode:ar";
    public static final String TYPE_CITY = "city";
    public static final String MODE_DRIVE = "drive";
    public static final String LANG_ES = "es";

    // Query params
    public static final String QUERY_PARAM_FILTER = "filter";
    public static final String QUERY_PARAM_API_KEY = "apiKey";
    public static final String QUERY_PARAM_TEXT = "text";
    public static final String QUERY_PARAM_TYPE = "type";
    public static final String QUERY_PARAM_WAYPOINTS = "waypoints";
    public static final String QUERY_PARAM_MODE = "mode";
    public static final String QUERY_PARAM_LANG = "lang";
    public static final String QUERY_PARAM_BIAS = "bias";

}