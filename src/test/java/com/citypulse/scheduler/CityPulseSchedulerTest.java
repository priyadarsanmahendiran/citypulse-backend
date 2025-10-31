package com.citypulse.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CityPulseSchedulerTest {

    private CityPulseScheduler scheduler;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws Exception {
        scheduler = new CityPulseScheduler();

        // inject a mock RestTemplate
        Field rtField = CityPulseScheduler.class.getDeclaredField("restTemplate");
        rtField.setAccessible(true);
        rtField.set(scheduler, restTemplate);

        // set values for properties
        Field keyField = CityPulseScheduler.class.getDeclaredField("aqiApiKey");
        keyField.setAccessible(true);
        keyField.set(scheduler, "dummy-key");

        Field urlField = CityPulseScheduler.class.getDeclaredField("aqiApiUrl");
        urlField.setAccessible(true);
        urlField.set(scheduler, "https://example.com/api");
    }

    @Test
    void fetchCityPulseDoesNotThrow() {
        assertDoesNotThrow(() -> scheduler.fetchCityPulse());
    }
}
