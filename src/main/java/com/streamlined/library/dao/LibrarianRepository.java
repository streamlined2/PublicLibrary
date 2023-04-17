package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Librarian;

@Repository
public interface LibrarianRepository extends CrudRepository<Librarian, Long> {

}
