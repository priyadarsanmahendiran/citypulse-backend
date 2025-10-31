package com.citypulse.CityPulseApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;

@SpringBootApplication
@Cacheable
public class CityPulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityPulseApplication.class, args);
	}

}
