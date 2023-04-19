package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Claim;

@Repository
public interface ClaimRepository extends CrudRepository<Claim, Long> {

}
