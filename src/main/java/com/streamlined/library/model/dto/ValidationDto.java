package com.streamlined.library.model.dto;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

import lombok.Builder;

@Builder
public record ValidationDto(Long id, ClaimDto claim, ManagerDto manager, LocalDateTime createdTime,
		MonetaryAmount compensation) {

}
