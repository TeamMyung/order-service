package com.sparta.orderservice.global.client;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailResponseDto {

	private UUID productId;
	private String productName;
	private int price;
	private int stock;
	private String status;
	private UUID hubId;
	private UUID vendorId;
	private String vendorName;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime approvedAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
}
