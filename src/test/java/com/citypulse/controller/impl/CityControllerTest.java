package com.citypulse.controller.impl;

import com.citypulse.controller.interfaces.ICityController;
import com.citypulse.model.response.CitySummary;
import com.citypulse.service.impl.CityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @InjectMocks
    CityController controller;

    @Mock
    CityService cityService;

    @Test
    void implementationShouldImplementInterface() {
        assertTrue(ICityController.class.isAssignableFrom(CityController.class));
    }

    @Test
    void getAllCitiesReturnsEmptyList() {
        Mockito.when(cityService.getAllCities()).thenReturn(Collections.emptyList());
        var list = controller.getAllCities();
        assertNotNull(list, "getAllCities() should not return null");
        assertTrue(list.isEmpty(), "getAllCities() should return an empty list by default");
    }

    @Test
    void getCitySummaryReturnsNullForNow() {
        CitySummary summary = controller.getCitySummary("any");
        assertNull(summary, "getCitySummary() currently returns null");
    }

    @Test
    void implementationHasRestControllerAnnotation() {
        var ann = CityController.class.getAnnotation(RestController.class);
        assertNotNull(ann, "CityController implementation should be annotated with @RestController");
    }
}
