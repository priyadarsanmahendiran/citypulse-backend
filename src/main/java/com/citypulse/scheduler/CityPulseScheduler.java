package com.citypulse.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@EnableScheduling
public class CityPulseScheduler {

    private RestTemplate restTemplate;

    @Value("{aqi.api.key}")
    private String aqiApiKey;

    @Value("{aqi.api.url}")
    private String aqiApiUrl;

    @Scheduled(cron = "*/15 * * * * *")
    public void fetchCityPulse() {
        log.info("Fetching data from external source...");

    }
}
