package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Return;

@Repository
public interface ReturnRepository extends CrudRepository<Return, Long> {

	@Query("select r from Return r where r.customer.id = :customerId")
	Iterable<Return> getReturns(@Param("customerId") Long customerId);

}
