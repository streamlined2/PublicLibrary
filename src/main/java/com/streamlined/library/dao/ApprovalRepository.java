package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Approval;

@Repository
public interface ApprovalRepository extends CrudRepository<Approval, Long> {
	
	@Query("select a from Approval a join a.request r")
	Streamable<Approval> getApprovedRequests();//TODO elaborate and fix

}
