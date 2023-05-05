package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Transfer;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("""
			select t
			from Transfer t join t.approval a join a.request r
			where r.customer.id = :customerId
			order by t.createdTime desc
			""")
	Streamable<Transfer> getCustomerTransfers(@Param("customerId") Long customerId);

}
