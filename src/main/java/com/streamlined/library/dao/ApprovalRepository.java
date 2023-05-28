package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Approval;
import com.streamlined.library.model.Book;

@Repository
public interface ApprovalRepository extends CrudRepository<Approval, Long> {

	@Query("select a from Approval a join a.request r")
	Streamable<Approval> getApprovedRequests();

	@Query("""
			select distinct b
			from Approval a join a.request t join a.books b
			where not exists (
			   select r
			   from Return r join r.books b2
			   where b2 = b and r.customer = t.customer and r.createdTime > a.createdTime
			)
			""")
	Streamable<Book> getNonAvailableBooks();

}
