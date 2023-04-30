package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Manager;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Long> {

}
