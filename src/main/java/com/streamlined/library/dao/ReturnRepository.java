package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Return;

@Repository
public interface ReturnRepository extends CrudRepository<Return, Long> {

}
