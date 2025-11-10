package com.sparta.orderservice.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderListResponseDto {
	private UUID orderId;
	private String productName;
	private UUID producerId;
	private UUID receiverId;
	private int quantity;
	private int totalPrice;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime deliveredAt;
}
