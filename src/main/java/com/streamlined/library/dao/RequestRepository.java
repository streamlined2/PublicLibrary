package com.streamlined.library.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import com.streamlined.library.model.Request;
import com.streamlined.library.model.dto.CategoryDataDto;

@Repository
public interface RequestRepository extends CrudRepository<Request, Long> {

	@Query("select a from Request a") // TODO should be elaborated to skip non-active requests
	Streamable<Request> findActiveRequests();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(b.genre, count(b.id)) from Request r join r.books b group by b.genre order by count(b.id) desc")
	Streamable<CategoryDataDto> getGenreData();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(b.country.name, count(b.id), 0) from Request r join r.books b group by b.country.name order by count(b.id) desc")
	Streamable<CategoryDataDto> getCountryData();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(b.language.name, count(b.id), 0) from Request r join r.books b group by b.language.name order by count(b.id) desc")
	Streamable<CategoryDataDto> getLanguageData();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(year(b.publishDate), count(b.id)) from Request r join r.books b group by year(b.publishDate) order by count(b.id) desc")
	Streamable<CategoryDataDto> getPublishYearData();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(b.size, count(b.id)) from Request r join r.books b group by b.size order by count(b.id) desc")
	Streamable<CategoryDataDto> getSizeData();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(b.cover.type, count(b.id)) from Request r join r.books b group by b.cover.type order by count(b.id) desc")
	Streamable<CategoryDataDto> getCoverTypeData();

	@Query("select new com.streamlined.library.model.dto.CategoryDataDto(b.cover.surface, count(b.id)) from Request r join r.books b group by b.cover.surface order by count(b.id) desc")
	Streamable<CategoryDataDto> getCoverSurfaceData();

}
