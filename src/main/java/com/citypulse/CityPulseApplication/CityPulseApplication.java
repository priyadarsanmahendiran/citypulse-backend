package com.citypulse.CityPulseApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = {"com.citypulse"})
public class CityPulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityPulseApplication.class, args);
	}

}
