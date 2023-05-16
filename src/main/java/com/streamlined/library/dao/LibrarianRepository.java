package com.streamlined.library.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Librarian;

@Repository
public interface LibrarianRepository extends CrudRepository<Librarian, Long> {
	
	public Optional<Librarian> findByLogin(String login);

}
