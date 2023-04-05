package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

}
