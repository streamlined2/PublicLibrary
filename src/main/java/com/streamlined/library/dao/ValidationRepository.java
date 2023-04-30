package com.streamlined.library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.streamlined.library.model.Validation;

@Repository
public interface ValidationRepository extends CrudRepository<Validation, Long> {

	@Query("select v from Validation v join v.claim c where c.id = :claimId")
	Optional<Validation> findByClaim(@Param("claimId") Long claimId);

}
