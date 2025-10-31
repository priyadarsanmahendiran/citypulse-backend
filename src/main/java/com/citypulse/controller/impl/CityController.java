package com.citypulse.controller.impl;

import com.citypulse.controller.interfaces.ICityController;
import com.citypulse.model.response.CitySummary;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController implements ICityController {
    @Override
    public List<String> getAllCities() {
        return List.of();
    }

    @Override
    public CitySummary getCitySummary(String cityName) {
        return null;
    }
}
