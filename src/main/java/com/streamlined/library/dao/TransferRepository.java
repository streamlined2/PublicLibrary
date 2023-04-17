package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Transfer;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Long> {

}
