package com.streamlined.library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.Review;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {

	@Query("select distinct t.books from Transfer t join t.approval a join a.request r where r.customer = :customer ")
	Streamable<Book> getReceivedBooks(@Param("customer") Customer customer);

	@Query("select r from Review r where r.book.id = :bookId")
	Streamable<Review> getBookReviews(@Param("bookId") Long bookId);

	@Query("select r from Review r where r.customer = :customer and r.book.id = :bookId")
	Optional<Review> getBookReview(@Param("bookId") Long bookId, @Param("customer") Customer customer);

}
