package com.citypulse.config;

import com.citypulse.enums.MetricType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class MetricConfig {

    @Value("${aqi.api.url:https://dummy-aqi-api.com}")
    private String aqiApiUrl;

    @Value("${aqi.api.key:DUMMY}")
    private String aqiApiKey;

    @Value("${weather.api.url:https://dummy-weather-api.com}")
    private String weatherApiUrl;

    @Value("${traffic.api.url:https://dummy-traffic-api.com}")
    private String trafficApiUrl;

    private Map<MetricType, String> apiTemplates;

    @PostConstruct
    public void init() {
        apiTemplates = Map.of(
                MetricType.WEATHER, weatherApiUrl + "/v1/forecast?latitude={lat}&longitude={lon}&current_weather=true",
                MetricType.AQI, aqiApiUrl + "/{city}/?token=" + "YOUR_API_KEY",
                MetricType.TRAFFIC, trafficApiUrl + "/v1/flow?lat={lat}&lon={lon}&apikey=YOUR_KEY"
        );
    }

    private final Map<MetricType, Integer> refreshIntervalMinutes = Map.of(
            MetricType.WEATHER, 5,
            MetricType.AQI, 15,
            MetricType.TRAFFIC, 5
    );

    public String getApiTemplate(MetricType metric) {
        return apiTemplates.get(metric);
    }

    public int getRefreshInterval(MetricType metric) {
        return refreshIntervalMinutes.get(metric);
    }
}