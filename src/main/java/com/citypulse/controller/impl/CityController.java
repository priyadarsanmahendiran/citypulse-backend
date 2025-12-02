package com.citypulse.controller.impl;

import com.citypulse.controller.interfaces.ICityController;
import com.citypulse.model.response.CityDetails;
import com.citypulse.model.response.CitySummary;
import com.citypulse.service.interfaces.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController implements ICityController {

    @Autowired private ICityService cityService;

    @Override
    public ResponseEntity<List<CityDetails>> getAllCities() {
        List<CityDetails> cityDetails = cityService.getAllCities();
        if(CollectionUtils.isEmpty(cityDetails)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cityDetails);
    }

    @Override
    public CitySummary getCitySummary(String cityName) {
        return cityService.getCitySummary(cityName);
    }
}
