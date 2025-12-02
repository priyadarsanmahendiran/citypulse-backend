package com.citypulse.service.impl;

import com.citypulse.config.MetricConfig;
import com.citypulse.enums.MetricType;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class IngestionService {

    private final Firestore firestore;
    private final RestTemplate restTemplate = new RestTemplate();
    private final MetricConfig metricConfig;

    public IngestionService(Firestore firestore, MetricConfig metricConfig) {
        this.firestore = firestore;
        this.metricConfig = metricConfig;
    }

    @Scheduled(fixedRateString = "${citypulse.ingest.rate-ms:60000}")
    public void ingestMetrics() {
        try {
            ApiFuture<QuerySnapshot> citiesFuture = firestore.collection("cities")
                    .whereEqualTo("active", true)
                    .get();
            List<QueryDocumentSnapshot> cities = citiesFuture.get().getDocuments();

            for (QueryDocumentSnapshot cityDoc : cities) {
                processCityMetrics(cityDoc);
            }
        } catch (Exception e) {
            log.error("Error during ingestion: {}", e.getMessage());
        }
    }

    private void processCityMetrics(DocumentSnapshot cityDoc) {
        String coordinates = "coords";
        String cityId = cityDoc.getId();
        GeoPoint geoPoint = cityDoc.getGeoPoint(coordinates);
        if(Objects.isNull(geoPoint))
            return;
        double lat = geoPoint.getLatitude();
        double lon = geoPoint.getLongitude();

        for (MetricType metric : MetricType.values()) {
            try {
                // check if we should fetch based on refresh interval
                // (optional: can store last fetch timestamp per metric to throttle)

                String url = buildApiUrl(metric, lat, lon, cityId, metricConfig);
                ResponseEntity<Map> resp = restTemplate.getForEntity(url, Map.class);
                Map<String, Object> body = resp.getBody();

                long now = System.currentTimeMillis();
                Map<String, Object> reading = new HashMap<>();
                reading.put("timestamp", now);
                reading.put("value", body);
                reading.put("quality", "ok");
                reading.put("retrieved_at", com.google.cloud.Timestamp.now());

                firestore.collection("cities")
                        .document(cityId)
                        .collection("metrics")
                        .document(metric.name().toLowerCase())
                        .collection("readings")
                        .document(String.valueOf(now))
                        .set(reading);

                log.info("Ingested {} for {}", metric, cityId);

            } catch (Exception e) {
                log.error("Failed {} for {}: {}", metric, cityId, e.getMessage());
            }
        }
    }

    private String buildApiUrl(MetricType metric, double lat, double lon, String city, MetricConfig config) {
        String template = config.getApiTemplate(metric);
        return template.replace("{lat}", String.valueOf(lat))
                .replace("{lon}", String.valueOf(lon))
                .replace("{city}", city); // Placeholder for city name if needed
    }
}
