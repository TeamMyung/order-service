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
import com.sparta.orderservice.global.client.ProductResponseDto;
import com.sparta.orderservice.global.exception.ErrorCode;
import com.sparta.orderservice.repository.OrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorOrderService {

	private final ProductClient productClient;
	private final OrderRepository orderRepository;

	@Transactional
	public OrderResponseDto createOrder(CreateOrderRequestDto requestDto, UUID vendorId) {

		ProductResponseDto product = productClient.getProductById(requestDto.getProductId());
		if (product == null) {
			throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getDetails());
		}

		if (product.getStock() < requestDto.getQuantity()) {
			throw new IllegalArgumentException("재고가 부족합니다. (현재 재고: " + product.getStock() + ")");
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
