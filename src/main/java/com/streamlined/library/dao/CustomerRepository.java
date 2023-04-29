package com.streamlined.library.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Customer;
import com.streamlined.library.model.dto.CustomerRequestDataDto;
import com.streamlined.library.model.dto.CustomerReviewDataDto;
import com.streamlined.library.model.dto.CustomerSummaryDataDto;
import com.streamlined.library.model.dto.CustomerTimeDataDto;

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
			select new com.streamlined.library.model.dto.CustomerSummaryDataDto(
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
	Streamable<CustomerSummaryDataDto> getSummaryCustomerData(LocalDate border1, LocalDate border2, LocalDate border3,
			LocalDate border4);

	@Query("""
			select new com.streamlined.library.model.dto.CustomerRequestDataDto(b.genre,l.name,c.name,count(b))
			from Request r join r.customer t join r.books b join b.language l join b.country c
			where t.id = :customerId
			group by b.genre,l.name,c.name
			order by count(b) desc
			""")
	Streamable<CustomerRequestDataDto> getCustomerRequestData(@Param("customerId") Long customerId);

	@Query("""
			select new com.streamlined.library.model.dto.CustomerTimeDataDto(
				b1.genre,
				l.name,
				c.name,
				sum(n.createdTime-f.createdTime) as time)
			from
				Return n join
				Transfer f join
				f.approval a join
				a.request r join
				r.customer t join
				n.books b1 join
				f.books b2 join
				b1.language l join
				b1.country c
			on b1.id = b2.id and n.customer = r.customer and n.createdTime > f.createdTime
			where t.id = :customerId
			group by b1.genre,l.name,c.name
			order by time desc
			""")
	Streamable<CustomerTimeDataDto> getCustomerTimeData(@Param("customerId") Long customerId);

	@Query("""
			select new com.streamlined.library.model.dto.CustomerReviewDataDto(
				b.genre,
				n.name,
				l.name,
				avg((case
					when r.rating = 'POOR' then 1 
					when r.rating = 'FAIR' then 2 
					when r.rating = 'FINE' then 3 
					when r.rating = 'GOOD' then 4 
					when r.rating = 'EXCELLENT' then 5 
					else 0
				end)) as average
			)
			from Review r join r.customer c join r.book b join b.country n join b.language l
			where c.id = :customerId
			group by r.rating,b.genre,n.name,l.name
			order by average desc
			""")
	Streamable<CustomerReviewDataDto> getCustomerReviewData(@Param("customerId") Long customerId);

}
