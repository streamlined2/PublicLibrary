package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
