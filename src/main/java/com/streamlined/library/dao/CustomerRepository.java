package com.streamlined.library.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.dto.CustomerDataDto;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("""
			select r.customer
			from Transfer t join t.approval a join a.request r
			where :book member of t.books
			order by t.createdTime desc
			""")
	Streamable<Customer> getBookHolders(@Param("book") Book book);

	@Query("""
			select new com.streamlined.library.model.dto.CustomerDataDto(
				c.sex,
				(case
					when c.birthDate < ?1 then '0'
					when ?1 <= c.birthDate and c.birthDate < ?2 then '1'
					when ?2 <= c.birthDate and c.birthDate < ?3 then '2'
					when ?3 <= c.birthDate and c.birthDate < ?4 then '3'
					else '4'
				end) as type,
				count(c))
			from Customer c
			group by c.sex, type
			order by c.sex, type
			""")
	Streamable<CustomerDataDto> getCustomerData(LocalDate border1, LocalDate border2, LocalDate border3,
			LocalDate border4);

}
