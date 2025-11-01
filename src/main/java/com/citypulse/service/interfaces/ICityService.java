package com.citypulse.service.interfaces;

import com.citypulse.model.response.CityDetails;
import com.citypulse.model.response.CitySummary;

import java.util.List;

public interface ICityService {
    List<CityDetails> getAllCities();

    CitySummary getCitySummary(String cityName);
}
