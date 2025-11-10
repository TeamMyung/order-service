package com.sparta.orderservice.service.admin;

import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.orderservice.global.client.ProductClient;
import com.sparta.orderservice.global.client.ProductDetailResponseDto;
import com.sparta.orderservice.entity.OrderEntity;
import com.sparta.orderservice.entity.enums.OrderStatus;
import com.sparta.orderservice.global.exception.CustomException;
import com.sparta.orderservice.repository.OrderRepository;
import com.sparta.orderservice.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminOrderService {

	private final ProductClient productClient;
	private final OrderRepository orderRepository;

	@Transactional
	public void updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID adminId) {
		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (order.getOrderStatus() != OrderStatus.PENDING) {
			throw new IllegalStateException("이미 처리된 주문입니다.");
		}
		order.setOrderStatus(newStatus);
		order.setApprovedBy(adminId.toString());
		orderRepository.save(order);

		//kafka 이벤트
	}
}
