package com.sparta.orderservice.controller.admin;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.orderservice.dto.response.OrderListResponseDto;
import com.sparta.orderservice.entity.enums.OrderStatus;
import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.service.OrderListService;
import com.sparta.orderservice.service.OrderStatusService;
import com.sparta.orderservice.service.admin.AdminOrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/admin")
public class AdminOrderController {

	private final AdminOrderService adminOrderService;
	private final OrderListService orderListService;
	private final OrderStatusService orderStatusService;

	@Operation(summary = "전체 주문 조회", description = "관리자가 전체 주문 내역을 조회합니다.")
	@GetMapping
	public ResponseEntity<ApiResponse<Page<OrderListResponseDto>>> getAllOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Page<OrderListResponseDto> response = orderListService.getAllOrders(page, size);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}

	@Operation(summary = "관리자 주문 상태 변경 API", description = "관리자가 주문을 승인 또는 거절합니다.")
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<ApiResponse<String>> updateOrderStatus(
		@PathVariable UUID orderId,
		@RequestParam("status") OrderStatus newStatus
	) {
		// 관리자 임시 ID (로그인 연동 전)
		UUID adminId = UUID.fromString("99999999-9999-9999-9999-999999999999");

		orderStatusService.updateOrderStatus(orderId, newStatus, adminId, null, true);
		return ResponseEntity.ok(new ApiResponse<>("주문 상태가 " + newStatus + "로 변경되었습니다."));
	}
}
