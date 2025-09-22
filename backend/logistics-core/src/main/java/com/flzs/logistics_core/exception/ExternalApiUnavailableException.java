package com.flzs.logistics_core.exception;

public class ExternalApiUnavailableException extends ExternalApiException {
    public ExternalApiUnavailableException(String apiName, String endpoint, Throwable cause) {
        super(String.format("%s unavailable at %s", apiName, endpoint), cause);
    }
}
