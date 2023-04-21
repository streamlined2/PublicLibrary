package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Transfer;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Long> {

	@Query("select distinct b from Transfer t join t.books b where t.approval.request.customer.id = :customerId")
	Iterable<Book> getCustomerBooks(@Param("customerId") Long customerId);//TODO exclude books returned by customer

}
