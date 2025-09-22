package com.flzs.logistics_core.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.flzs.logistics_core.exception.*;
import com.flzs.logistics_core.model.domain.Coordinates;
import com.flzs.logistics_core.model.dto.geoapify.*;
import com.flzs.logistics_core.model.enums.AddressAccuracy;
import com.flzs.logistics_core.model.routing.RouteInfo;
import com.flzs.logistics_core.util.calculator.DistanceCalculator;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

import static com.flzs.logistics_core.util.constants.GeoApifyParams.*;

@Component
public class GeoApifyClient {

    private static final Logger logger = LoggerFactory.getLogger(GeoApifyClient.class);
    private final WebClient webClient;

    @Value("${geo-apify.api.key}")
    private String apiKey;

    public GeoApifyClient(WebClient.Builder builder, @Value("${geo-apify.max-response-size}") int maxResponseSize) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(config ->
                        config.defaultCodecs().maxInMemorySize(maxResponseSize)
                )
                .build();
        this.webClient = builder
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.newConnection() // Creates new connections each time
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
                                .responseTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT_SECONDS))
                ))
                .exchangeStrategies(strategies)
                .build();
    }

    /**
     * Retrieves municipality center coordinates for proximity-based address filtering.
     * Used as a reference point to validate that geocoded addresses fall within
     * reasonable distance of the expected municipality.
     *
     * @param municipalityName Municipality name to geocode
     * @param provinceName     Province name for disambiguation
     * @return Coordinates of the municipality center
     * @throws LocationNotFoundException       if municipality cannot be found
     * @throws ExternalApiUnavailableException if GeoApify service is unavailable
     */
    public Coordinates getCoordinatesFromMunicipality(String municipalityName, String provinceName) {
        try {
            String searchText = municipalityName + ", " + provinceName + ", Argentina";
            GeoApifyGeocodeResponse geocodeResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(GEOCODE_PATH)
                            .queryParam(QUERY_PARAM_TEXT, searchText)
                            .queryParam(QUERY_PARAM_FILTER, COUNTRY_CODE_AR)
                            .queryParam(QUERY_PARAM_TYPE, TYPE_CITY)
                            .queryParam(QUERY_PARAM_API_KEY, apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(GeoApifyGeocodeResponse.class)
                    .block();

            if (geocodeResponse == null || geocodeResponse.features() == null || geocodeResponse.features().isEmpty()) {
                throw new LocationNotFoundException(
                        "Could not find coordinates for municipality: " + municipalityName + ", " + provinceName);
            }

            var coordinates = geocodeResponse.features().get(0).geometry().coordinates();
            logger.info("Municipality coordinates found: lat={}, lon={} for {}, {}",
                    coordinates.get(1), coordinates.get(0), municipalityName, provinceName);
            return new Coordinates(coordinates.get(1), coordinates.get(0));

        } catch (LocationNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error getting municipality coordinates: {}", e.getMessage());
            throw new ExternalApiUnavailableException("GeoApify", GEOCODE_PATH, e);
        }
    }

    /**
     * Calculates driving route between two coordinate points.
     * Returns distance, estimated time, turn-by-turn instructions, and route geometry.
     *
     * @param origin      Starting point coordinates
     * @param destination End point coordinates
     * @return RouteInfo containing route details and navigation instructions
     * @throws RouteCalculationException       if route cannot be calculated
     * @throws ExternalApiUnavailableException if GeoApify routing service is unavailable
     */
    public RouteInfo getRouteBetweenCoordinates(Coordinates origin, Coordinates destination) {
        try {
            String waypoints = String.format(Locale.US, "%f,%f|%f,%f",
                    origin.latitude(), origin.longitude(),
                    destination.latitude(), destination.longitude());

            GeoApifyRouteResponse routeResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(ROUTING_PATH)
                            .queryParam(QUERY_PARAM_WAYPOINTS, waypoints)
                            .queryParam(QUERY_PARAM_MODE, MODE_DRIVE)
                            .queryParam(QUERY_PARAM_LANG, LANG_ES)
                            .queryParam(QUERY_PARAM_API_KEY, apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(GeoApifyRouteResponse.class)
                    .block();

            if (routeResponse == null || routeResponse.features() == null || routeResponse.features().isEmpty()) {
                throw new RouteCalculationException(
                        "GeoApify could not calculate the route between " + origin + " and " + destination);
            }

            var props = routeResponse.features().get(0).properties();
            if (props == null || props.legs() == null || props.legs().isEmpty()) {
                throw new RouteCalculationException(
                        "GeoApify returned invalid route data between " + origin + " and " + destination);
            }

            var geometry = routeResponse.features().get(0).geometry();
            List<List<Double>> routeCoordinates = List.of();
            if (geometry != null && geometry.coordinates() != null) {
                routeCoordinates = extractCoordinates(geometry.coordinates(), geometry.type());
            }

            var steps = props.legs().get(0).steps();
            List<String> instructions = (steps != null)
                    ? steps.stream()
                    .map(s -> s.instruction() != null ? s.instruction().text() : "")
                    .filter(t -> !t.isBlank())
                    .toList()
                    : List.of();

            logger.info("Route found: distance={}m, time={}s, legs={}",
                    props.distance(), props.time(), instructions.size());

            return new RouteInfo(props.distance(), props.time(), instructions, routeCoordinates);
        } catch (RouteCalculationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error calling GeoApify API (getRouteBetweenCoordinates): {}", e.getMessage());
            throw new ExternalApiUnavailableException("GeoApify routing service", ROUTING_PATH, e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<List<Double>> extractCoordinates(Object coordinates, String geometryType) {
        if (coordinates instanceof List<?> coors && !coors.isEmpty()) {
            if ("LineString".equals(geometryType)) {
                return (List<List<Double>>) coors;
            } else if ("MultiLineString".equals(geometryType)) {
                return (List<List<Double>>) coors.get(0);
            }
        }
        return List.of();
    }

    /**
     * Resolves detailed address coordinates with proximity filtering.
     * Searches for the given address within the specified municipality and validates
     * that results are within acceptable distance (2km) from the municipality center.
     *
     * @param address          The street address to geocode
     * @param municipalityName Municipality name for context filtering
     * @param provinceName     Province name for context filtering
     * @return ResolvedLocation with coordinates and accuracy assessment
     * @throws LocationNotFoundException       if municipality is not found
     * @throws ExternalApiUnavailableException if GeoApify service is unavailable
     */
    public ResolvedLocation geocodeAddressWithProximityValidation(String address, String municipalityName, String provinceName) {
        try {
            Coordinates municipalityCenter = getCoordinatesFromMunicipality(municipalityName, provinceName);
            JsonNode geocodeResponse = performGeocodeRequest(address, municipalityCenter);
            return processGeocodeResponse(geocodeResponse, address, municipalityCenter, municipalityName, provinceName);
        } catch (LocationNotFoundException e) {
            logger.warn("Municipality not found: {}, {} - {}", municipalityName, provinceName, e.getMessage());
            throw new LocationNotFoundException(municipalityName, provinceName);
        } catch (Exception e) {
            logger.error("Error calling GeoApify API (geocodeAddressWithProximityValidation(...)): {}", e.getMessage());
            throw new ExternalApiUnavailableException("GeoApify geocoding service", GEOCODE_PATH, e);
        }
    }

    private JsonNode findBestMatchByProximity(JsonNode features, Coordinates municipalityCenter, String targetMunicipality, String targetProvince) {
        JsonNode bestMatch = null;
        double closestDistance = Double.MAX_VALUE;

        for (JsonNode feature : features) {
            var geom = feature.get("geometry");
            if (geom == null) continue;

            var coordinates = geom.get("coordinates");
            if (coordinates == null || coordinates.size() < 2) continue;

            double lon = coordinates.get(0).asDouble();
            double lat = coordinates.get(1).asDouble();

            double distance = DistanceCalculator.calculateDistance(
                    municipalityCenter.latitude(), municipalityCenter.longitude(), lat, lon);

            // Accept any result within reasonable distance
            if (distance <= MAX_ACCEPTABLE_DISTANCE_METERS && distance < closestDistance) {
                closestDistance = distance;
                bestMatch = feature;
            }
        }

        logger.info("Best proximity match found at {}m from municipality center", String.format("%.2f", closestDistance));
        return bestMatch;
    }

    private ResolvedLocation createFallbackLocation(String address) {
        return new ResolvedLocation(null,
                AddressAccuracy.FALLBACK,
                0.0,
                "fallback",
                address,
                NO_DISTANCE_AVAILABLE // No distance available for fallback
        );
    }

    private AddressAccuracy mapToPermissiveAddressAccuracy(double confidence, String matchType, String resultType, double distanceFromCenterMeters) {
        if ("city".equals(resultType) || "locality".equals(resultType) ||
                "match_by_city_or_disrict".equalsIgnoreCase(matchType) || // Yep, it's "disrict" and not "district" for some reason
                "match_by_postcode".equalsIgnoreCase(matchType) ||
                "postcode".equals(resultType)) {
            logger.warn("Rejecting non-street-specific result: matchType={}, resultType={}", matchType, resultType);
            return AddressAccuracy.FALLBACK;
        }

        // Distance check (only for street-level results that passed the above filter)
        if (distanceFromCenterMeters >= 0 && distanceFromCenterMeters <= MAX_ACCEPTABLE_DISTANCE_METERS) {
            if (distanceFromCenterMeters < CENTER_THRESHOLD_METERS) {
                logger.warn("Rejecting result at exact municipality center - likely fallback");
                return AddressAccuracy.FALLBACK;
            }

            if ("street".equals(resultType) || "building".equals(resultType) || "amenity".equals(resultType)) {
                logger.info("Accepting street-level result within distance limit: {}m", String.format("%.0f", distanceFromCenterMeters));
                return AddressAccuracy.APPROXIMATE;
            }
        }

        // If distance exceeds 2000 meters, reject immediately!
        if (distanceFromCenterMeters > MAX_ACCEPTABLE_DISTANCE_METERS) {
            logger.warn("Rejecting result - distance {}m exceeds 2000m limit", String.format("%.0f", distanceFromCenterMeters));
            return AddressAccuracy.FALLBACK;
        }

        // Fallback for cases where distance is not available (distanceFromCenterMeters == -1)
        if (distanceFromCenterMeters == NO_DISTANCE_AVAILABLE && confidence >= HIGH_CONFIDENCE_THRESHOLD &&
                ("street".equals(resultType) || "building".equals(resultType) || "amenity".equals(resultType))) {
            logger.info("Accepting street-level result without distance info based on high confidence: {}", confidence);
            return AddressAccuracy.APPROXIMATE;
        }

        // Default fallback
        logger.warn("Rejecting result - insufficient criteria met: distance={}m, confidence={}, resultType={}",
                String.format("%.2f", distanceFromCenterMeters), confidence, resultType);
        return AddressAccuracy.FALLBACK;
    }

    private JsonNode performGeocodeRequest(String address, Coordinates municipalityCenter) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(GEOCODE_PATH)
                        .queryParam(QUERY_PARAM_TEXT, address)
                        .queryParam(QUERY_PARAM_FILTER, COUNTRY_CODE_AR)
                        .queryParam(QUERY_PARAM_BIAS, String.format(Locale.US, "proximity:%f,%f",
                                municipalityCenter.longitude(), municipalityCenter.latitude()))
                        .queryParam("filter", String.format(Locale.US, "circle:%f,%f,100000",
                                municipalityCenter.longitude(), municipalityCenter.latitude()))
                        .queryParam(QUERY_PARAM_API_KEY, apiKey)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private ResolvedLocation processGeocodeResponse(JsonNode root, String address, Coordinates municipalityCenter,
                                                    String municipalityName, String provinceName) {
        var features = root != null ? root.get("features") : null;
        if (features == null || !features.isArray() || features.isEmpty()) {
            logger.warn("GeoApify did not find detailed coordinates for: {} in {}, {}", address, municipalityName, provinceName);
            return createFallbackLocation(address);
        }

        var validFeature = findBestMatchByProximity(features, municipalityCenter, municipalityName, provinceName);
        if (validFeature == null) {
            logger.warn("No address found within 2km radius for: {} in {}, {}", address, municipalityName, provinceName);
            return createFallbackLocation(address);
        }

        return buildResolvedLocation(validFeature, address, municipalityCenter, municipalityName, provinceName);
    }

    private ResolvedLocation buildResolvedLocation(JsonNode validFeature, String address, Coordinates municipalityCenter,
                                                   String municipalityName, String provinceName) {
        var props = validFeature.get("properties");
        var geom = validFeature.get("geometry");
        var coordinates = geom.get("coordinates");

        double lon = coordinates.get(0).asDouble();
        double lat = coordinates.get(1).asDouble();
        double confidence = props.path("rank").path("confidence").asDouble(0.0);
        String matchType = props.path("rank").path("match_type").asText("");
        String resultType = props.path("result_type").asText("");
        String formatted = props.path("formatted").asText(address);

        double distanceFromCenter = DistanceCalculator.calculateDistance(
                municipalityCenter.latitude(), municipalityCenter.longitude(), lat, lon);

        if (distanceFromCenter < CENTER_THRESHOLD_METERS) {
            logger.warn("Result is at exact municipality center - likely no specific address found for: {}", address);
        }

        AddressAccuracy accuracy = mapToPermissiveAddressAccuracy(confidence, matchType, resultType, distanceFromCenter);

        logger.info("Geocode '{}' in {},{} -> lat={}, lon={}, conf={}, matchType={}, resultType={}, distance={}m, accuracy={} {}",
                address, municipalityName, provinceName, lat, lon, confidence, matchType, resultType,
                String.format("%.0f", distanceFromCenter), accuracy,
                (accuracy == AddressAccuracy.FALLBACK ? "[REJECTED!]" : "[ACCEPTED!]"));

        return new ResolvedLocation(new Coordinates(lat, lon), accuracy, confidence, matchType, formatted, distanceFromCenter);
    }
}