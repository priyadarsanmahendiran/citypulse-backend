package com.citypulse.service.impl;

import com.citypulse.model.response.CityDetails;
import com.citypulse.model.response.CitySummary;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @InjectMocks
    CityService service;

    @Mock
    Firestore firestore;

    @Mock
    CollectionReference citiesCollection;

    @Mock
    Query citiesQuery;

    @Mock
    ApiFuture<QuerySnapshot> citiesFuture;

    @Mock
    QuerySnapshot citiesSnapshot;

    @Mock
    QueryDocumentSnapshot cityDoc;

    @Test
    void getAllCitiesReturnsEmptyListByDefault() throws Exception {
        when(firestore.collection("cities")).thenReturn(citiesCollection);
        when(citiesCollection.whereEqualTo("active", true)).thenReturn(citiesQuery);
        when(citiesQuery.get()).thenReturn(citiesFuture);
        when(citiesFuture.get()).thenReturn(citiesSnapshot);
        when(citiesSnapshot.getDocuments()).thenReturn(Collections.emptyList());

        var list = service.getAllCities();
        assertNotNull(list);
        assertTrue(list.isEmpty(), "Expected empty list when Firestore has no active cities");
    }

    @Test
    void getAllCitiesReturnsValue() throws Exception {
        when(firestore.collection("cities")).thenReturn(citiesCollection);
        when(citiesCollection.whereEqualTo("active", true)).thenReturn(citiesQuery);
        when(citiesQuery.get()).thenReturn(citiesFuture);
        when(citiesFuture.get()).thenReturn(citiesSnapshot);

        when(cityDoc.getData()).thenReturn(Map.of("country", "TestCountry"));
        when(cityDoc.getId()).thenReturn("city123");
        when(citiesSnapshot.getDocuments()).thenReturn(List.of(cityDoc));

        var list = service.getAllCities();
        assertNotNull(list);
        assertEquals(1, list.size());
        CityDetails details = list.get(0);
        assertEquals("TestCountry", details.country());
        assertEquals("city123", details.name());
    }

    @Test
    void getCitySummaryReturnsSummary() throws Exception {
        String cityName = "city123";

        // mocks for city document and metrics
        DocumentReference cityRef = mock(DocumentReference.class);
        CollectionReference metricsCollection = mock(CollectionReference.class);
        ApiFuture<QuerySnapshot> metricsFuture = mock(ApiFuture.class);
        QuerySnapshot metricsSnapshot = mock(QuerySnapshot.class);
        QueryDocumentSnapshot metricDoc = mock(QueryDocumentSnapshot.class);
        DocumentReference metricDocRef = mock(DocumentReference.class);

        when(firestore.collection("cities")).thenReturn(citiesCollection);
        when(citiesCollection.document(cityName)).thenReturn(cityRef);

        when(cityRef.collection("metrics")).thenReturn(metricsCollection);
        when(metricsCollection.get()).thenReturn(metricsFuture);
        when(metricsFuture.get()).thenReturn(metricsSnapshot);
        when(metricsSnapshot.getDocuments()).thenReturn(List.of(metricDoc));

        when(metricDoc.getId()).thenReturn("aqi");
        when(metricDoc.getReference()).thenReturn(metricDocRef);

        // mocks for latest reading
        CollectionReference readingsRef = mock(CollectionReference.class);
        Query latestQuery = mock(Query.class);
        ApiFuture<QuerySnapshot> latestFuture = mock(ApiFuture.class);
        QuerySnapshot latestSnapshot = mock(QuerySnapshot.class);
        QueryDocumentSnapshot latestReading = mock(QueryDocumentSnapshot.class);

        when(metricDocRef.collection("readings")).thenReturn(readingsRef);
        when(readingsRef.orderBy("timestamp", Query.Direction.DESCENDING)).thenReturn(latestQuery);
        when(latestQuery.limit(1)).thenReturn(latestQuery);
        when(latestQuery.get()).thenReturn(latestFuture);
        when(latestFuture.get()).thenReturn(latestSnapshot);
        when(latestSnapshot.getDocuments()).thenReturn(List.of(latestReading));

        Map<String, Object> latestData = new HashMap<>();
        latestData.put("value", 42);
        when(latestReading.getData()).thenReturn(latestData);

        CitySummary summary = service.getCitySummary(cityName);
        assertNotNull(summary);
        assertEquals(cityName, summary.name());
        assertEquals(42, summary.aqi());
    }
}
