package com.citypulse.repository;

import com.citypulse.model.entity.CityEntity;
import org.springframework.data.repository.CrudRepository;

public interface ICityRepository extends CrudRepository<CityEntity,Integer> {}
