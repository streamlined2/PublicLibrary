package com.streamlined.library.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

}
