package com.sparta.orderservice.controller.hub;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/hub")
@Tag(name = "Hub 주문 API", description = "허브관리자(HUB)용 주문 관련 API")
public class HubOrderController {

	private final OrderListService orderListService;
	private final OrderStatusService orderStatusService;

	@Operation(summary = "허브 주문 전체 조회", description = "허브 관리자가 자신의 허브에 관련된 모든 주문 조회")
	@GetMapping
	public ResponseEntity<ApiResponse<Page<OrderListResponseDto>>> getHubOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
		//@AuthenticationPrincipal UserAuth userAuth
	) {
		// TODO: JWT 연동 시 hubId = userAuth.getHubId()
		UUID fakeHubId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		Page<OrderListResponseDto> response = orderListService.getHubOrders(fakeHubId, page, size);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}

	@Operation(summary = "허브 주문 상태 변경 API", description = "허브가 자신의 허브 주문을 승인 또는 거절합니다.")
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<ApiResponse<String>> updateOrderStatus(
		@PathVariable UUID orderId,
		@RequestParam("status") OrderStatus newStatus
	) {
		// 허브 임시 ID (로그인 연동 전)
		UUID hubId = UUID.fromString("22222222-2222-2222-2222-222222222222");
		UUID approverId = hubId; // 허브 자체를 승인자로 설정

		orderStatusService.updateOrderStatus(orderId, newStatus, approverId, hubId, false);
		return ResponseEntity.ok(new ApiResponse<>("주문 상태가 " + newStatus + "로 변경되었습니다."));
	}
}
