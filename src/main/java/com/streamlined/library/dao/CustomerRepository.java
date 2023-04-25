package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("select r.customer from Transfer t join t.approval a join a.request r where :book member of t.books order by t.createdTime desc")
	Streamable<Customer> getBookHolders(@Param("book") Book book);

}
