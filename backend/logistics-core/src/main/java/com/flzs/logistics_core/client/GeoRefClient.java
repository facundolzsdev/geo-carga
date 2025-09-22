package com.flzs.logistics_core.client;

import com.flzs.logistics_core.exception.ExternalApiUnavailableException;
import com.flzs.logistics_core.model.dto.georef.raw.*;
import io.netty.channel.ChannelOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;

import static com.flzs.logistics_core.util.constants.GeoRefParams.*;

@Component
public class GeoRefClient {

    private static final Logger logger = LoggerFactory.getLogger(GeoRefClient.class);
    private final WebClient webClient;

    public GeoRefClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.newConnection() // Creates new connections each time
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
                                .responseTimeout(Duration.ofSeconds(RESPONSE_TIMEOUT_SECONDS))
                )).build();
    }

    public GeoRefProvinceResponse getProvinces() {
        try {
            GeoRefProvinceResponse prResponse = webClient.get()
                    .uri(PATH_PROVINCES)
                    .retrieve()
                    .bodyToMono(GeoRefProvinceResponse.class)
                    .block();

            if (prResponse == null) {
                return new GeoRefProvinceResponse(List.of());
            }

            return prResponse;
        } catch (Exception e) {
            logger.error("Error calling GeoRef API (getProvinces): {}", e.getMessage());
            throw new ExternalApiUnavailableException("GeoRef Client", PATH_PROVINCES, e);
        }
    }

    public GeoRefDepartmentResponse getDepartmentsByProvince(String provinceID) {
        try {
            GeoRefDepartmentResponse deptResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(PATH_DEPARTMENTS)
                            .queryParam(QUERY_PARAM_PROVINCE, provinceID)
                            .queryParam(QUERY_PARAM_FIELDS, "id,nombre,provincia")
                            .queryParam(QUERY_PARAM_MAX, MAX_RESULTS)
                            .build())
                    .retrieve()
                    .bodyToMono(GeoRefDepartmentResponse.class)
                    .block();

            if (deptResponse == null) {
                return new GeoRefDepartmentResponse(List.of());
            }

            return deptResponse;
        } catch (Exception e) {
            logger.error("Error calling GeoRef API (getDepartmentsByProvince): {}", e.getMessage());
            throw new ExternalApiUnavailableException("GeoRef Client", PATH_DEPARTMENTS, e);
        }
    }

    /**
     * Retrieves municipalities filtered by province and department.
     * Used to populate municipality dropdowns in the shipping form.
     *
     * @param provinceID   Province identifier for filtering
     * @param departmentID Department identifier for filtering
     * @return Response containing list of municipalities within the specified department
     * @throws ExternalApiUnavailableException if GeoRef service is unavailable
     */
    public GeoRefMunicipalityResponse getMunicipalitiesByProvinceAndDepartment(String provinceID, String departmentID) {
        try {
            GeoRefMunicipalityResponse munResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(PATH_MUNICIPALITIES)
                            .queryParam(QUERY_PARAM_PROVINCE, provinceID)
                            .queryParam(QUERY_PARAM_INTERSECTION, "departamento:" + departmentID)
                            .queryParam(QUERY_PARAM_FIELDS, "id,nombre,provincia")
                            .queryParam(QUERY_PARAM_MAX, MAX_RESULTS)
                            .build())
                    .retrieve()
                    .bodyToMono(GeoRefMunicipalityResponse.class)
                    .block();

            if (munResponse == null) {
                return new GeoRefMunicipalityResponse(List.of());
            }

            return munResponse;
        } catch (Exception e) {
            logger.error("Error calling GeoRef API (getMunicipalitiesByProvinceAndDepartment): {}", e.getMessage());
            throw new ExternalApiUnavailableException("GeoRef Client", PATH_MUNICIPALITIES, e);
        }
    }

}