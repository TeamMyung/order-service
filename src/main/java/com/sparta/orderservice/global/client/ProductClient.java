package com.sparta.orderservice.global.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

	@GetMapping("/v1/products/{productId}")
	ProductResponseDto getProductById(@PathVariable("productId") UUID productId);
}
