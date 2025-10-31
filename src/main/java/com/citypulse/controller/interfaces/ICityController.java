package com.citypulse.controller.interfaces;

import com.citypulse.model.response.CitySummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/cities")
public interface ICityController {

    @GetMapping
    List<String> getAllCities();

    @GetMapping("{cityName}/summary")
    CitySummary getCitySummary(@PathVariable("cityName") String cityName);
}
