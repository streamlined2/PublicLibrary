package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Request;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

}
