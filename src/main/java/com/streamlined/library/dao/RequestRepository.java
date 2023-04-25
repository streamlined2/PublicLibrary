package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Request;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {
	
	@Query("select a from Request a")//TODO should be elaborated to skip non-active requests
	Streamable<Request> findActiveRequests();

}
