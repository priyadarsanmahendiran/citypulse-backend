package com.citypulse.model.external.aqi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AQIResponse (
        @JsonProperty("status") String status,
        @JsonProperty("city") City city,
        @JsonProperty("aqi") int aqi,
        @JsonProperty("dominentpol") String dominantPollutant
) {}