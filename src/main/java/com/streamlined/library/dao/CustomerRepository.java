package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;

import com.streamlined.library.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
