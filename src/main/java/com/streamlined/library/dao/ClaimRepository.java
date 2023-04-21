package com.streamlined.library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Claim;

@Repository
public interface ClaimRepository extends CrudRepository<Claim, Long> {

	@Query("select c from Claim c join c.bookReturn r where r.id = :returnId and c.book.id = :bookId")
	Optional<Claim> getClaim(Long returnId, Long bookId);

}
