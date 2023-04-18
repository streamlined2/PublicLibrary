package com.streamlined.library.model.mapper;

import org.springframework.stereotype.Component;

import com.streamlined.library.model.Review;
import com.streamlined.library.model.dto.ReviewDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewMapper implements Mapper<ReviewDto, Review> {

	private final BookMapper bookMapper;
	private final CustomerMapper customerMapper;

	@Override
	public Review toEntity(ReviewDto dto) {
		return Review.builder().id(dto.id()).book(bookMapper.toEntity(dto.book()))
				.customer(customerMapper.toEntity(dto.customer())).createdTime(dto.createdTime())
				.updatedTime(dto.updatedTime()).rating(dto.rating()).text(dto.text()).build();
	}

	@Override
	public ReviewDto toDto(Review entity) {
		return ReviewDto.builder().id(entity.getId()).book(bookMapper.toDto(entity.getBook()))
				.customer(customerMapper.toDto(entity.getCustomer())).createdTime(entity.getCreatedTime())
				.updatedTime(entity.getUpdatedTime()).rating(entity.getRating()).text(entity.getText()).build();
	}

}
