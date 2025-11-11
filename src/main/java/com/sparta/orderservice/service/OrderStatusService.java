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

@Service
@RequiredArgsConstructor
public class OrderStatusService {

	private final OrderRepository orderRepository;

	@Transactional
	public void updateOrderStatus(UUID orderId, OrderStatus newStatus, UUID approverId, UUID hubId, boolean isAdmin) {

		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!isAdmin) {
			if (order.getReceiverId() == null || !order.getReceiverId().equals(hubId)) {
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
