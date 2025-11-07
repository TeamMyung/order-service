package com.sparta.orderservice.global.client;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponseDto {
	private String productId;
	private String productName;
	private String status;
	private String message;
	private int stock;
	private int price;
	private LocalDateTime createdAt;
	private LocalDateTime approvedAt;
	private String approvedBy;
}
