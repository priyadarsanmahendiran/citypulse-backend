package com.citypulse.service.impl;

import com.citypulse.model.entity.CityEntity;
import com.citypulse.repository.ICityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @InjectMocks
    CityService service;

    @Mock
    ICityRepository cityRepository;

    @Test
    void implementationShouldImplementInterface() {
        assertTrue(com.citypulse.service.interfaces.ICityService.class.isAssignableFrom(CityService.class));
    }

    @Test
    void getAllCitiesReturnsEmptyListByDefault() {
        Mockito.when(cityRepository.findAll()).thenReturn(Collections.emptyList());
        var list = service.getAllCities();
        assertTrue(list.isEmpty(), "getAllCities() should return an empty list by default");
    }

    @Test
    void getAllCitiesReturnsValue() {
        List<CityEntity> cityEntities = new ArrayList<>();
        cityEntities.add(new CityEntity(10, "testCity", "testCountry", 100.0, 200.0, LocalDateTime.now()));
        Mockito.when(cityRepository.findAll()).thenReturn(cityEntities);
        var list = service.getAllCities();
        assertFalse(list.isEmpty(), "getAllCities() should return an valid list");
    }

}
