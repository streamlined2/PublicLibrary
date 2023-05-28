package com.streamlined.library.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Request;
import com.streamlined.library.model.dto.CategoryRequestDataDto;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

	@Query("select a from Request a")
	Streamable<Request> findActiveRequests();

	@Query("""
			select r from Request r join fetch r.books b
			where r.id = :requestId
			""")
	Optional<Request> getRequestById(@Param("requestId") Long requestId);

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(b.genre, count(b.id))
			from Request r join r.books b
			group by b.genre
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getGenreData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(b.country.name, count(b.id))
			from Request r join r.books b
			group by b.country.name
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getCountryData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(b.language.name, count(b.id))
			from Request r join r.books b
			group by b.language.name
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getLanguageData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(year(b.publishDate), count(b.id))
			from Request r join r.books b
			group by year(b.publishDate)
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getPublishYearData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(b.size, count(b.id))
			from Request r join r.books b
			group by b.size
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getSizeData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(b.cover.type, count(b.id))
			from Request r join r.books b
			group by b.cover.type
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getCoverTypeData();

	@Query("""
			select new com.streamlined.library.model.dto.CategoryRequestDataDto(b.cover.surface, count(b.id))
			from Request r join r.books b
			group by b.cover.surface
			order by count(b.id) desc
			""")
	Streamable<CategoryRequestDataDto> getCoverSurfaceData();

}
