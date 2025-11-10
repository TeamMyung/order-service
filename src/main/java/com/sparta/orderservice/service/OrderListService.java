package com.sparta.orderservice.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.orderservice.dto.response.OrderListResponseDto;
import com.sparta.orderservice.entity.OrderEntity;
import com.sparta.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListService {

	private final OrderRepository orderRepository;

	@Transactional(readOnly = true)
	public Page<OrderListResponseDto> getVendorBuyOrders(UUID vendorId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<OrderEntity> orders = orderRepository.findByReceiverId(vendorId, pageable);

		return orders.map(this::convertToDto);
	}

	@Transactional(readOnly = true)
	public Page<OrderListResponseDto> getVendorSalesOrders(UUID vendorId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<OrderEntity> orders = orderRepository.findByProducerId(vendorId, pageable);

		return orders.map(this::convertToDto);
	}

	@Transactional(readOnly = true)
	public Page<OrderListResponseDto> getHubOrders(UUID hubId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<OrderEntity> orders = orderRepository.findByHubId(hubId, pageable);

		return orders.map(this::convertToDto);
	}

	@Transactional(readOnly = true)
	public Page<OrderListResponseDto> getAllOrders(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<OrderEntity> orders = orderRepository.findAll(pageable);

		return orders.map(this::convertToDto);
	}

	private OrderListResponseDto convertToDto(OrderEntity order) {
		int totalPrice = 0;
		try {
			totalPrice = order.getQuantity() *
				// productName, productPrice 등 추가정보를 외부에서 가져오는 경우 이곳 수정
				1;
		} catch (Exception e) {
			log.warn("총 가격 계산 중 오류 발생: {}", e.getMessage());
		}

		return OrderListResponseDto.builder()
			.orderId(order.getOrderId())
			.productName(order.getProductName())
			.producerId(order.getProducerId())
			.receiverId(order.getReceiverId())
			.quantity(order.getQuantity())
			.totalPrice(totalPrice)
			.status(order.getOrderStatus().name())
			.createdAt(order.getCreatedAt())
			.deliveredAt(order.getUpdatedAt()) // 실제 배송일자 컬럼 있으면 변경
			.build();
	}
}
