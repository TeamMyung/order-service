package com.sparta.orderservice.global.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparta.orderservice.global.config.FeignConfig;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {

	@GetMapping("/v1/internal/products/{productId}")
	ProductDetailResponseDto getProductById(@PathVariable("productId") UUID productId);

	@PutMapping("/v1/internal/products/{productId}/decrease-stock")
	void decreaseStock(
		@PathVariable("productId") UUID productId,
		@RequestParam("quantity") int quantity
	);

	@PutMapping("/v1/internal/products/{productId}/increase-stock")
	void increaseStock(
		@PathVariable UUID productId,
		@RequestParam("quantity") int quantity);
}