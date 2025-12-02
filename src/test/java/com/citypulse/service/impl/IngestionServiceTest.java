package com.citypulse.service.impl;

import com.citypulse.config.MetricConfig;
import com.citypulse.enums.MetricType;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngestionServiceTest {

    @InjectMocks
    IngestionService ingestionService;

    @Mock
    Firestore firestore;

    @Mock
    QuerySnapshot citiesSnapshot;

    @Mock
    MetricConfig metricConfig;

    @Mock
    ApiFuture<QuerySnapshot> citiesFuture;

    @Mock
    QueryDocumentSnapshot cityDoc;

    @Mock
    CollectionReference citiesCollection;

    @Mock
    Query citiesQuery;

    @BeforeEach
    void setup() throws Exception {
        // default: have firestore.collection("cities") return citiesCollection
        when(firestore.collection("cities")).thenReturn(citiesCollection);
        when(citiesCollection.whereEqualTo("active", true)).thenReturn(citiesQuery);
        when(citiesQuery.get()).thenReturn(citiesFuture);

        // default metric config template
        when(metricConfig.getApiTemplate(any())).thenReturn("http://example.com/{lat}/{lon}/{city}");

        // inject a mock RestTemplate to avoid real HTTP calls
        RestTemplate rtMock = mock(RestTemplate.class);
        when(rtMock.getForEntity(anyString(), eq(Map.class))).thenReturn(ResponseEntity.ok(Map.of("x", 1)));

        Field rtField = IngestionService.class.getDeclaredField("restTemplate");
        rtField.setAccessible(true);
        rtField.set(ingestionService, rtMock);
    }

    @Test
    void ingestMetricsWritesReadingsForEachMetric() throws Exception {
        // prepare a city doc with coordinates and id
        when(citiesFuture.get()).thenReturn(citiesSnapshot);
        when(citiesSnapshot.getDocuments()).thenReturn(List.of(cityDoc));
        when(cityDoc.getId()).thenReturn("city1");
        when(cityDoc.getGeoPoint("coords")).thenReturn(new GeoPoint(12.34, 56.78));

        // prepare write chain: firestore.collection("cities").document(cityId)... -> readingDoc
        DocumentReference cityDocRef = mock(DocumentReference.class);
        CollectionReference metricsCollection = mock(CollectionReference.class);
        DocumentReference metricDocRef = mock(DocumentReference.class);
        CollectionReference readingsCollection = mock(CollectionReference.class);
        DocumentReference readingDocRef = mock(DocumentReference.class);

        when(citiesCollection.document("city1")).thenReturn(cityDocRef);
        when(cityDocRef.collection("metrics")).thenReturn(metricsCollection);
        // document(...) will be called with metric name; return same mock for any
        when(metricsCollection.document(anyString())).thenReturn(metricDocRef);
        when(metricDocRef.collection("readings")).thenReturn(readingsCollection);
        when(readingsCollection.document(anyString())).thenReturn(readingDocRef);
        when(readingDocRef.set(any())).thenReturn(mock(ApiFuture.class));

        // execute
        ingestionService.ingestMetrics();

        // expect that .set(...) was called once per MetricType
        int metricCount = MetricType.values().length;
        verify(readingDocRef, atLeast(metricCount)).set(any());
    }
}

