package com.sparta.orderservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sparta.orderservice.entity.OrderEntity;
import com.sparta.orderservice.entity.enums.OrderStatus;
import com.sparta.orderservice.global.exception.CustomException;
import com.sparta.orderservice.global.exception.ErrorCode;
import com.sparta.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusService {

	private final OrderRepository orderRepository;

	@Transactional
	public void updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID approverId, UUID hubId, boolean isAdmin) {

		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		log.info("요청한 허브 hubId: {}", hubId);
		log.info("주문에 저장된 hubId: {}", order.getHubId());

		if (!isAdmin) {
			if (order.getHubId() == null || !order.getHubId().equals(hubId)) {
				throw new CustomException(ErrorCode.ORDER_ACCESS_DENIED);
			}
		}

		if (order.getOrderStatus() != OrderStatus.PENDING) {
			if (order.getOrderStatus() == OrderStatus.CANCELED) {
				throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELED);
			} else if (order.getOrderStatus() == OrderStatus.ACCEPTED) {
				throw new CustomException(ErrorCode.ORDER_ALREADY_ACCEPTED);
			}
		}

		order.setOrderStatus(newStatus);
		order.setApprovedBy(approverId.toString());
		orderRepository.save(order);

		//kafka 이벤트
	}
}
