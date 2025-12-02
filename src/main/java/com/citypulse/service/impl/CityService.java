package com.citypulse.service.impl;

import com.citypulse.model.response.CityDetails;
import com.citypulse.model.response.CitySummary;
import com.citypulse.service.interfaces.ICityService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class CityService implements ICityService {

    @Autowired private Firestore firestore;

    @Override
    public List<CityDetails> getAllCities() {
        try {
            ApiFuture<QuerySnapshot> citiesFuture = firestore.collection("cities")
                    .whereEqualTo("active", true)
                    .get();
            List<QueryDocumentSnapshot> cities = citiesFuture.get().getDocuments();
            List<Map<String, Object>> cityList = new ArrayList<>();

            for (QueryDocumentSnapshot cityDoc : cities) {
                Map<String, Object> cityData = new HashMap<>(cityDoc.getData());
                cityData.put("id", cityDoc.getId());
                cityList.add(cityData);
            }

           List<CityDetails> cityDetails = new ArrayList<>();
           for (Map<String, Object> city : cityList) {
               CityDetails details = new CityDetails(
                       (String) city.get("id"),
                       (String) city.get("country")
               );
               cityDetails.add(details);
           }

           return cityDetails;

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error fetching cities: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public CitySummary getCitySummary(String cityName) {
        Map<String, Object> result = new HashMap<>();
        try {
            DocumentReference cityRef = firestore.collection("cities").document(cityName);
            ApiFuture<QuerySnapshot> metricsFuture = cityRef.collection("metrics").get();
            List<QueryDocumentSnapshot> metrics = metricsFuture.get().getDocuments();

            for (QueryDocumentSnapshot metricDoc : metrics) {
                String metricId = metricDoc.getId(); // e.g., "weather", "aqi"

                // Get latest reading for this metric
                CollectionReference readingsRef = metricDoc.getReference().collection("readings");
                Query latestQuery = readingsRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(1);
                ApiFuture<QuerySnapshot> latestFuture = latestQuery.get();
                List<QueryDocumentSnapshot> latestDocs = latestFuture.get().getDocuments();

                if (!latestDocs.isEmpty()) {
                    result.put(metricId, latestDocs.getFirst().getData().get("value"));
                } else {
                    result.put(metricId, Collections.singletonMap("status", "no-data"));
                }
            }

            int aqiValue = -1;
            if (result.containsKey("aqi")) {
                Object aqiObj = result.get("aqi");
                aqiValue = ((Number) aqiObj).intValue();
            }

            return new CitySummary(cityName, aqiValue);

        } catch (InterruptedException | ExecutionException e) {
            log.error("Error fetching city: {}", e.getMessage());
            return null;
        }
    }
}
