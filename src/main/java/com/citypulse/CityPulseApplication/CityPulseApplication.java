package com.citypulse.CityPulseApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = {"com.citypulse"})
@EntityScan(basePackages = {"com.citypulse"})
@ComponentScan(basePackages = {"com.citypulse"})
public class CityPulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityPulseApplication.class, args);
	}

}
