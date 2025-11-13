package com.sparta.orderservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelResponseDto {

	private UUID orderId;

	private UUID productId;

	private String productName;

	private String orderStatus;

	private LocalDateTime createdAt;

	private LocalDateTime canceledAt;

	private LocalDateTime updatedAt;
}
