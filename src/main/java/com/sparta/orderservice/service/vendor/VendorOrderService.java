package com.sparta.orderservice.service.vendor;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.orderservice.dto.request.CreateOrderRequestDto;
import com.sparta.orderservice.dto.response.OrderCancelResponseDto;
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

		ApiResponse<ProductDetailResponseDto> response;
		ProductDetailResponseDto product;

		try {
			product = productClient.getProductById(requestDto.getProductId());
		}
		catch (feign.FeignException.ServiceUnavailable e) {
			throw new CustomException(ErrorCode.PRODUCT_SERVICE_UNAVAILABLE);
		}
		catch (feign.FeignException.NotFound e) {
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND_ORDER);
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
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND_ORDER);
		}

		if (product.getStock() < requestDto.getQuantity()) {
			throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
		}

		try {
			productClient.decreaseStock(product.getProductId(), requestDto.getQuantity());
		} catch (Exception e) {
			log.error("상품 재고 감소 실패: {}", e.getMessage());
			throw new CustomException(ErrorCode.PRODUCT_STOCK_UPDATE_FAILED);
		}

		try {
			UUID deliveryId = UUID.randomUUID();

			OrderEntity order = OrderEntity.builder()
				.producerId(product.getVendorId())
				.receiverId(vendorId)
				.hubId(product.getHubId())
				.productId(requestDto.getProductId())
				.productName(product.getProductName())
				.quantity(requestDto.getQuantity())
				.request(requestDto.getRequest())
				.orderStatus(OrderStatus.PENDING)
				.deliveryId(deliveryId)
				//.deliveryStatus(DeliveryStatus.PENDING)
				.build();

			orderRepository.save(order);

			int totalPrice = product.getPrice() * requestDto.getQuantity();

			return OrderResponseDto.builder()
				.orderId(order.getOrderId())
				.productId(order.getProductId())
				.productName(product.getProductName())
				.orderStatus(order.getOrderStatus().name())
				//order.getDeliveryStatus().name(),
				.createdAt(order.getCreatedAt())
				.deliveryId(order.getDeliveryId())
				.totalPrice(totalPrice)
				.build();
		} catch (Exception e) {
			log.error("주문 생성 실패: {}", e.getMessage());
			throw new CustomException(ErrorCode.ORDER_CREATION_FAILED);
		}
	}

	@Transactional
	public OrderCancelResponseDto cancelOrder(UUID orderId, UUID vendorId) {
		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		if (!order.getReceiverId().equals(vendorId)) {
			throw new CustomException(ErrorCode.ORDER_ACCESS_DENIED);
		}

		switch (order.getOrderStatus()) {
			case PENDING:
				order.updateOrderStatus(OrderStatus.CANCELED);
				try {
					productClient.increaseStock(order.getProductId(), order.getQuantity());
				} catch (Exception e) {
					log.error("상품 재고 복구 실패: {}", e.getMessage());
					throw new CustomException(ErrorCode.PRODUCT_STOCK_RESTORE_FAILED);
				}
				break;
			case CANCELED:
				throw new CustomException(ErrorCode.ORDER_ALREADY_CANCELED);
			default:
				throw new CustomException(ErrorCode.ORDER_CANNOT_CANCEL);
		}

		try {
			orderRepository.save(order);
		} catch (Exception e) {
			log.error("주문 취소 중 DB 오류: {}", e.getMessage());
			throw new CustomException(ErrorCode.ORDER_CANCELLATION_FAILED);
		}

		return OrderCancelResponseDto.builder()
			.orderId(order.getOrderId())
			.productId(order.getProductId())
			.productName(order.getProductName())
			.orderStatus(order.getOrderStatus().name())
			.createdAt(order.getCreatedAt())
			.canceledAt(LocalDateTime.now())
			.updatedAt(order.getUpdatedAt())
			.build();
	}
}