package com.sparta.orderservice.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
	private UUID productId;
	private String productName;
	private UUID hubId;
	private int quantity;
	private String request;
}
