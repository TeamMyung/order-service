package com.sparta.orderservice.controller.admin;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.orderservice.entity.enums.OrderStatus;
import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.service.admin.AdminOrderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/admin")
public class AdminOrderController {

	private final AdminOrderService adminOrderService;

	@Operation(summary = "주문 상태 변경 API", description = "주문을 승인 또는 거절합니다.")
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<ApiResponse<String>> updateOrderStatus(
		@PathVariable UUID orderId,
		@RequestParam("status") OrderStatus newStatus
	) {
		//로그인 되면 제거
		UUID fakeAdminId = UUID.fromString("99999999-9999-9999-9999-999999999999");

		adminOrderService.updateOrderStatus(orderId, newStatus, fakeAdminId);
		return ResponseEntity.ok(new ApiResponse<>("주문 상태가 " + newStatus + "(으)로 변경되었습니다."));
	}
}
