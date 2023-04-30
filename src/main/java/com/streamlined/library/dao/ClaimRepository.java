package com.streamlined.library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Claim;

@Repository
public interface ClaimRepository extends CrudRepository<Claim, Long> {

	@Query("select c from Claim c join c.bookReturn r where r.id = :returnId and c.book.id = :bookId")
	Optional<Claim> getClaim(@Param("returnId") Long returnId, @Param("bookId") Long bookId);
	
	@Query("""
			select c 
			from 
				Claim c join fetch 
				c.bookReturn r join fetch 
				r.customer t join fetch 
				c.book b join fetch 
				c.librarian l
			""")
	Streamable<Claim> getAllClaims();

}
