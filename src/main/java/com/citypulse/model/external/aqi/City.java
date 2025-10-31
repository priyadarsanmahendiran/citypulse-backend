package com.citypulse.model.external.aqi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record City(
        @JsonProperty("geo") List<Double> coordinates,
        @JsonProperty("name") String name
) {
}
