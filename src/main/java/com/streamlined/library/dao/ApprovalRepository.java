package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Approval;

@Repository
public interface ApprovalRepository extends CrudRepository<Approval, Long> {

}
