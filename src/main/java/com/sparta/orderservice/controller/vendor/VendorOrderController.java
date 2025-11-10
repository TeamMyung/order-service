package com.sparta.orderservice.controller.vendor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.orderservice.dto.request.CreateOrderRequestDto;
import com.sparta.orderservice.dto.response.OrderCancelResponseDto;
import com.sparta.orderservice.dto.response.OrderListResponseDto;
import com.sparta.orderservice.dto.response.OrderResponseDto;
import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.service.OrderListService;
import com.sparta.orderservice.service.vendor.VendorOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/vendor")
@Tag(name = "Vendor 주문 API", description = "업체(VENDOR)용 주문 관련 API")
//@PreAuthorize("hasRole('ROLE_VENDOR')")
public class VendorOrderController {

	private final VendorOrderService vendorOrderService;
	private final OrderListService orderListService;

	@Operation(summary = "주문 생성 API", description = "업체(Vendor)가 상품을 주문합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
		@Valid @RequestBody CreateOrderRequestDto requestDto
		//,@AuthenticationPrincipal UserAuth userAuth
	) {
		//로그인 되면 제거
		UUID fakeVendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		OrderResponseDto response = vendorOrderService.createOrder(requestDto, fakeVendorId);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}

	@Operation(summary = "주문 취소 API", description = "업체(Vendor)가 주문을 취소합니다. (PENDING 상태만 가능)")
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<ApiResponse<OrderCancelResponseDto>> cancelOrder(
		@PathVariable UUID orderId
		//,@AuthenticationPrincipal UserAuth userAuth
	) {
		// 로그인 구현 전 임시 vendorId
		UUID fakeVendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		OrderCancelResponseDto response = vendorOrderService.cancelOrder(orderId, fakeVendorId);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}

	@Operation(summary = "구매 목록 조회", description = "해당 업체가 구매한 주문 목록을 조회합니다.")
	@GetMapping("/buys")
	public ResponseEntity<ApiResponse<Page<OrderListResponseDto>>> getVendorBuyOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
		//@AuthenticationPrincipal UserAuth userAuth
	) {
		// TODO: JWT 연동 시 vendorId = userAuth.getId()
		UUID fakeVendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Page<OrderListResponseDto> response = orderListService.getVendorBuyOrders(fakeVendorId, page, size);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}

	@Operation(summary = "판매 목록 조회", description = "해당 업체가 판매한 주문 목록을 조회합니다.")
	@GetMapping("/sales")
	public ResponseEntity<ApiResponse<Page<OrderListResponseDto>>> getVendorSalesOrders(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
		//@AuthenticationPrincipal UserAuth userAuth
	) {
		UUID fakeVendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		Page<OrderListResponseDto> response = orderListService.getVendorSalesOrders(fakeVendorId, page, size);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}
}
