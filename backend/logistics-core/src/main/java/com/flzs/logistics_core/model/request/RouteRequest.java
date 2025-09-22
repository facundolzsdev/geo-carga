package com.flzs.logistics_core.model.request;

import com.flzs.logistics_core.model.domain.Coordinates;

public record RouteRequest(Coordinates origin, Coordinates destination) { }

