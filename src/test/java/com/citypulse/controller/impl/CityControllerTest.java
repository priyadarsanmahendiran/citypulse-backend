package com.citypulse.controller.impl;

import com.citypulse.controller.interfaces.ICityController;
import com.citypulse.model.response.CityDetails;
import com.citypulse.model.response.CitySummary;
import com.citypulse.service.interfaces.ICityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @InjectMocks
    CityController controller;

    @Mock
    ICityService cityService;

    @Test
    void implementationShouldImplementInterface() {
        assertTrue(ICityController.class.isAssignableFrom(CityController.class));
    }

    @Test
    void getAllCitiesReturnsNoContentWhenEmpty() {
        when(cityService.getAllCities()).thenReturn(Collections.emptyList());
        ResponseEntity<List<CityDetails>> resp = controller.getAllCities();
        assertNotNull(resp);
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        assertNull(resp.getBody());
    }

    @Test
    void getAllCitiesReturnsOkWhenNonEmpty() {
        List<CityDetails> list = List.of(new CityDetails("city123", "CountryX"));
        when(cityService.getAllCities()).thenReturn(list);
        ResponseEntity<List<CityDetails>> resp = controller.getAllCities();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().size());
        assertEquals("city123", resp.getBody().get(0).name());
    }

    @Test
    void getCitySummaryDelegatesToService() {
        CitySummary cs = new CitySummary("city123", 10);
        when(cityService.getCitySummary("city123")).thenReturn(cs);
        CitySummary out = controller.getCitySummary("city123");
        assertSame(cs, out);
    }
}
