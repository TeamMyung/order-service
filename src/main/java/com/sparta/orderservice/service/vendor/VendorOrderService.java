package com.sparta.orderservice.service.vendor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.orderservice.dto.request.CreateOrderRequestDto;
import com.sparta.orderservice.dto.response.OrderResponseDto;
import com.sparta.orderservice.entity.OrderEntity;
import com.sparta.orderservice.entity.enums.OrderStatus;
import com.sparta.orderservice.global.client.ProductClient;
import com.sparta.orderservice.global.client.ProductDetailResponseDto;
import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.global.exception.CustomException;
import com.sparta.orderservice.global.exception.ErrorCode;
import com.sparta.orderservice.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorOrderService {

	private final ProductClient productClient;
	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponseDto createOrder(CreateOrderRequestDto requestDto, UUID vendorId) {

		ProductDetailResponseDto product;

		try {
			product = productClient.getProductById(requestDto.getProductId());
		}
		catch (feign.FeignException.ServiceUnavailable e) {
			throw new CustomException(ErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
		}
		catch (feign.FeignException.NotFound e) {
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}
		catch (feign.FeignException e) {
			log.error("FeignException: status={}, body={}", e.status(), e.contentUTF8());
			throw new CustomException(ErrorCode.PRODUCT_RESPONSE_MAPPING_FAILED);
		}
		catch (Exception e) {
			log.error("Unexpected exception when calling product-service: {}", e.getMessage());
			throw new CustomException(ErrorCode.PRODUCT_RESPONSE_INVALID_FORMAT);
		}

		if (product == null) {
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
		}

		if (product.getStock() < requestDto.getQuantity()) {
			throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
		}

		OrderEntity order = OrderEntity.builder()
			.producerId(vendorId)
			.productId(requestDto.getProductId())
			.quantity(requestDto.getQuantity())
			.request(requestDto.getRequest())
			.orderStatus(OrderStatus.PENDING)
			//.deliveryStatus(DeliveryStatus.PENDING)
			.build();

		orderRepository.save(order);

		return OrderResponseDto.builder()
			.orderId(order.getOrderId())
			.productId(order.getProductId())
			.productName(product.getProductName())
			.orderStatus(order.getOrderStatus().name())
			//order.getDeliveryStatus().name(),
			.createdAt(order.getCreatedAt())
			.build();
	}
}
