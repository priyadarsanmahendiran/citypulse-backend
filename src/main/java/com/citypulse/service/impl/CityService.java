package com.citypulse.service.impl;

import com.citypulse.model.entity.CityEntity;
import com.citypulse.model.response.CityDetails;
import com.citypulse.model.response.CitySummary;
import com.citypulse.repository.ICityRepository;
import com.citypulse.service.interfaces.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityService implements ICityService {

    @Autowired private ICityRepository cityRepository;

    @Override
    public List<CityDetails> getAllCities() {
        Iterable<CityEntity> cityEntities = cityRepository.findAll();
        List<CityDetails> cityDetails = new ArrayList<>();
        for(CityEntity cityEntity: cityEntities)
            cityDetails.add(new CityDetails(cityEntity.getCityName(), cityEntity.getCountry()));
        return cityDetails;
    }

    @Override
    public CitySummary getCitySummary(String cityName) {
        return null;
    }
}
