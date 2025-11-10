package com.sparta.orderservice.global.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.orderservice.global.config.FeignConfig;
import com.sparta.orderservice.global.dto.ApiResponse;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {

	@GetMapping("/v1/products/{productId}")
	ProductDetailResponseDto getProductById(@PathVariable("productId") UUID productId);
}
