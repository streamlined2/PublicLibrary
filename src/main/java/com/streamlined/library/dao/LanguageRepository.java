package com.streamlined.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Language;

@Repository
public interface LanguageRepository extends CrudRepository<Language, Long> {

}
