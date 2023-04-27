package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Book;
import com.streamlined.library.model.Transfer;
import com.streamlined.library.model.dto.CategoryTimeDataDto;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, Long> {

	@Query("select distinct b from Transfer t join t.books b where t.approval.request.customer.id = :customerId")
	Streamable<Book> getCustomerBooks(@Param("customerId") Long customerId);// TODO exclude books returned by customer

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(b.genre, sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by b.genre
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getGenreData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(b.country.name, sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by b.country.name
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getCountryData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(b.language.name, sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by b.language.name
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getLanguageData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(year(b.publishDate), sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by year(b.publishDate)
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getPublishYearData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(b.size, sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by b.size
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getSizeData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(b.cover.type, sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by b.cover.type
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getCoverTypeData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryTimeDataDto(b.cover.surface, sum(r.createdTime - t.createdTime))
			from Transfer t join t.books b join t.approval a join a.request q join Return r join r.books b2
			on r.customer = q.customer and r.createdTime > t.createdTime and b.id = b2.id
			group by b.cover.surface
			order by sum(r.createdTime - t.createdTime) desc
			""")
	Streamable<CategoryTimeDataDto> getCoverSurfaceData();

}
