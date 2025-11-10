package com.sparta.orderservice.controller.hub;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.orderservice.dto.response.OrderListResponseDto;
import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.service.OrderListService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/hub")
public class HubOrderController {

	private final OrderListService orderListService;

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
}
